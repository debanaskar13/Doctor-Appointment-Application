package site.debashisnaskar.rxflow.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private boolean success;
    private String message;
}
