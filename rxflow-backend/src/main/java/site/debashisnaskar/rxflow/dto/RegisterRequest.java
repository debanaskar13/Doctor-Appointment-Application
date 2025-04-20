package site.debashisnaskar.rxflow.dto;

import lombok.*;
import site.debashisnaskar.rxflow.model.Address;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String image;
    private String gender;
    private String dob;
    private Address address;

}
