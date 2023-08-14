package com.bank.aggregator.services;

import com.bank.aggregator.models.common.ApplicationRequest;
import com.bank.aggregator.models.common.Offer;

import java.util.Optional;

public interface BankService {
    Optional<Offer> submitApplication(ApplicationRequest applicationRequest);

}
