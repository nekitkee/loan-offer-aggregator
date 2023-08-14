package com.bank.aggregator.services;

import com.bank.aggregator.models.common.Application;
import com.bank.aggregator.models.common.ApplicationRequest;
import com.bank.aggregator.models.common.Offer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinancingService {
    private final List<BankService> bankServices;
    public Application apply(ApplicationRequest request){
        var futures = bankServices.stream()
                .map(bankService -> CompletableFuture.supplyAsync(
                        () -> bankService.submitApplication(request), ForkJoinPool.commonPool()))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        var allResults = allOf.thenApplyAsync(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()), ForkJoinPool.commonPool());
        try {
            List<Offer> offers = allResults.get().stream()
                    .flatMap(Optional::stream)
                    .toList();

            return Application.builder()
                    .request(request)
                    .offers(offers)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error while processing applications", e);
        }
    }
}
