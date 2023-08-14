package com.bank.aggregator.models.solidbank;

import com.bank.aggregator.models.ApplicationStatus;
import lombok.Data;

import java.util.Optional;

@Data
public class SbApplication {
    private String id;
    private ApplicationStatus status;
    private Optional<SbOffer> offer = Optional.empty();
}
