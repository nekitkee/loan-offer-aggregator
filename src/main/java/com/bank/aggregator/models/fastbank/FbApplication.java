package com.bank.aggregator.models.fastbank;

import com.bank.aggregator.models.ApplicationStatus;
import lombok.Data;

import java.util.Optional;

@Data
public class FbApplication {
    private String id;
    private ApplicationStatus status;
    private Optional<FbOffer> offer = Optional.empty();
}
