package org.beaconfire.application.entity;

import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
}
