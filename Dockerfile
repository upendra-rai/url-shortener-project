# ==========================================
# Build Stage
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Runtime Stage
# ==========================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create a non-root system user for security
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set file ownership
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Default port for Spring Boot
EXPOSE 8080

# Production-ready JVM memory and container flags
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]