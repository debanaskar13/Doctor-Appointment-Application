package site.debashisnaskar.rxflow.model;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Slot {
    private String slotDate;
    private String slotTime;
    private boolean isBooked;
}
