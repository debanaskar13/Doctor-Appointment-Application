package site.debashisnaskar.rxflow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {

    private String username;
    private String password;
    private String name;

}
