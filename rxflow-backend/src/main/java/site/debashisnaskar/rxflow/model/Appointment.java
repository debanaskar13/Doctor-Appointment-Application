package site.debashisnaskar.rxflow.model;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Appointment {
    private UUID id;
    private String slotDate;
    private String slotTime;
    private User user;
    private Doctor doctor;
    private double amount;
    private String date;
    private boolean cancelled;
    private boolean payment;
    private boolean isCompleted;
}
