package site.debashisnaskar.rxflow.model;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Appointment {
    private int id;
    private int userId;
    private int docId;
    private String slotDate;
    private String slotTime;
    private User user;
    private Doctor doctor;
    private double amount;
    private int date;
    private boolean cancelled;
    private boolean payment;
    private boolean isCompleted;
}
