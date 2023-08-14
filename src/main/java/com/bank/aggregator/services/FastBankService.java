package com.bank.aggregator.services;

import com.bank.aggregator.clients.FastBankClient;
import com.bank.aggregator.models.ApplicationStatus;
import com.bank.aggregator.models.common.ApplicationRequest;
import com.bank.aggregator.models.common.Offer;
import com.bank.aggregator.models.fastbank.FbApplication;
import com.bank.aggregator.models.fastbank.FbApplicationRequest;
import com.bank.aggregator.models.fastbank.FbOffer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class FastBankService implements BankService {
    private final FastBankClient fastBankClient;
    private final PollingService pollingService;
    @Override
    public Optional<Offer> submitApplication(ApplicationRequest request) {
        var fbApplication = fastBankClient.submitApplication(FbApplicationRequest.from(request));
        var applicationId = fbApplication.getId();

        Callable<FbApplication> apiCall = () -> {
            try {
                return fastBankClient.getApplicationById(applicationId);
            } catch (Exception e) {
                throw new RuntimeException("Error while calling apiCall", e);
            }
        };

        Predicate<FbApplication> statusPredicate = result ->
                result.getStatus().equals(ApplicationStatus.PROCESSED);

        var fbProcessedApplication = pollingService.poll(apiCall, statusPredicate);
        return fbProcessedApplication.getOffer().map(FbOffer::toOffer);
    }
}
