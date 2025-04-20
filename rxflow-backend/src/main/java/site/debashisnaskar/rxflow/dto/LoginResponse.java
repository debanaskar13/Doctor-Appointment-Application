package site.debashisnaskar.rxflow.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponse {

    private boolean success;
    private String token;
    private String role;

}
