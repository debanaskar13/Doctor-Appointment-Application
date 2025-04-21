package site.debashisnaskar.rxflow.dto;


import lombok.*;
import site.debashisnaskar.rxflow.model.Address;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto {
    private String name;
    private String image;
    private Address address;

}
