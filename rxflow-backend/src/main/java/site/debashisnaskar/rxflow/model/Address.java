package site.debashisnaskar.rxflow.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String line1;
    private String line2;
}
