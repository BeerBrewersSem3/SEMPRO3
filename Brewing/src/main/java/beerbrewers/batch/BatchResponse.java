package beerbrewers.batch;

import lombok.Data;

import java.util.List;

@Data
public class BatchResponse {
    private List<Batch> data;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
