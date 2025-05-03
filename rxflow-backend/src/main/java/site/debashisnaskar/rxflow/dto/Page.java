package site.debashisnaskar.rxflow.dto;


import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page <T> {

    private long totalPages;
    private long pageNumber;
    private long pageSize;
    private String sortBy;
    private String sorDirection;
    private T data;

}
