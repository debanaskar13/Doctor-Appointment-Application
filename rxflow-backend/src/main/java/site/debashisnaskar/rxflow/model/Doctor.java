package site.debashisnaskar.rxflow.model;


import lombok.*;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Doctor {
    private int id;
    private User user;
    private String speciality;
    private String degree;
    private String experience;
    private String about;
    private boolean available;
    private Double fees;
    private Map<String, ArrayList<String>> slotsBooked;
}
