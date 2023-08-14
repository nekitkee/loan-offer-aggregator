package com.bank.aggregator.services;

import com.bank.aggregator.clients.SolidBankClient;
import com.bank.aggregator.models.ApplicationStatus;
import com.bank.aggregator.models.common.ApplicationRequest;
import com.bank.aggregator.models.common.Offer;
import com.bank.aggregator.models.solidbank.SbApplication;
import com.bank.aggregator.models.solidbank.SbApplicationRequest;
import com.bank.aggregator.models.solidbank.SbOffer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class SolidBankService implements BankService{
    private final SolidBankClient client;
    private final PollingService pollingService;
    @Override
    public Optional<Offer> submitApplication(ApplicationRequest request) {
        var sbApplication = client.submitApplication(SbApplicationRequest.from(request));
        var applicationId = sbApplication.getId();

        Callable<SbApplication> apiCall = () -> {
            try {
                return client.getApplicationById(applicationId);
            } catch (Exception e) {
                throw new RuntimeException("Error while calling apiCall", e);
            }
        };

        Predicate<SbApplication> statusPredicate = result ->
                result.getStatus().equals(ApplicationStatus.PROCESSED);

        var sbProcessedApplication = pollingService.poll(apiCall, statusPredicate);

        return sbProcessedApplication.getOffer().map(SbOffer::toOffer);
    }
}
