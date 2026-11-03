package com.upendra.url.service.dtos;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageResponse<T> {

    private List<T> data;
    
    private int pageSize;

    private Long nextCursor;

    private boolean hasNext;

}
