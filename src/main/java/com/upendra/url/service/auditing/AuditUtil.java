package com.upendra.url.service.auditing;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.upendra.url.service.entities.AuditLog;

public class AuditUtil {

    private static final List<String> EXCLUDED_FIELDS = List.of("id", "leadId", "createdDate", "updatedDate");

    public static <T, U> List<AuditLog> generateAuditLogs(
            String entityName,
            String entityId,
            T oldObj,
            U newObj,
            String updatedBy) {

        List<AuditLog> auditLogs = new ArrayList<>();

        if (oldObj == null || newObj == null) {
            return auditLogs;
        }

        for (Field oldField : oldObj.getClass().getDeclaredFields()) {
            oldField.setAccessible(true);

            // Skip excluded fields
            if (EXCLUDED_FIELDS.contains(oldField.getName())) {
                continue;
            }

            // Only allow primitive, string, numbers, booleans, LocalDateTime
            if (!oldField.getType().isPrimitive()
                    && !String.class.equals(oldField.getType())
                    && !Number.class.isAssignableFrom(oldField.getType())
                    && !Boolean.class.equals(oldField.getType())
                    && !LocalDateTime.class.equals(oldField.getType())) {
                continue; // skip complex objects
            }

            try {
                // Try to find a matching field in newObj
                Field newField;
                try {
                    newField = newObj.getClass().getDeclaredField(oldField.getName());
                    newField.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    continue; // skip if newObj does not have this field
                }

                Object oldValue = oldField.get(oldObj);
                Object newValue = newField.get(newObj);

                if (!Objects.equals(oldValue, newValue)) {
                    AuditLog log = new AuditLog();
                    log.setEntityName(entityName);
                    log.setEntityId(entityId);
                    log.setFieldName(oldField.getName());
                    log.setOldValue(oldValue != null ? oldValue.toString() : null);
                    log.setNewValue(newValue != null ? newValue.toString() : null);
                    log.setChangedBy(updatedBy);
                    log.setChangedAt(LocalDateTime.now());
                    auditLogs.add(log);

                    // Sync oldObj field with new value (only if types match)
                    if (oldField.getType().isAssignableFrom(newField.getType())) {
                        oldField.set(oldObj, newValue);
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field: " + oldField.getName(), e);
            }
        }

        return auditLogs;
    }
}
