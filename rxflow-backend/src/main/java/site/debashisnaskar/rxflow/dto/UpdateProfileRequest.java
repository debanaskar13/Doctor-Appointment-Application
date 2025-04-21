package site.debashisnaskar.rxflow.dto;

import lombok.*;
import site.debashisnaskar.rxflow.model.Address;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UpdateProfileRequest {
    private String name;
    private String phone;
    private Address address;
    private String gender;
    private String dob;
}
