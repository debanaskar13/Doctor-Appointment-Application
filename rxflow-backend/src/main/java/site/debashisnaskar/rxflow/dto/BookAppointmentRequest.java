package site.debashisnaskar.rxflow.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookAppointmentRequest {

    private int userId;
    private int doctorId;
    private String slotDate;
    private String slotTime;

}
