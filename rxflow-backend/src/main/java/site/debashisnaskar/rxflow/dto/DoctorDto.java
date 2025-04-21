package site.debashisnaskar.rxflow.dto;

import lombok.*;
import site.debashisnaskar.rxflow.model.Slot;
import site.debashisnaskar.rxflow.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DoctorDto {

    private int id;
    private UserDto user;
    private String speciality;
    private String degree;
    private String experience;
    private String about;
    private boolean available;
    private Double fees;
    private Slot slotsBooked;

}
