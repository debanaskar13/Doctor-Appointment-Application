package site.debashisnaskar.rxflow.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String name;
}
