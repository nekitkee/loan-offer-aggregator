package com.bank.aggregator.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.function.Predicate;

@Service
public class PollingService {


    private final long DEFAULT_POLLING_MAX_TIME = 60000;
    private final long DEFAULT_POLLING_INTERVAL = 1000;

    public <T> T poll(Callable<T> apiCall, Predicate<T> statusPredicate){
        return poll(apiCall, statusPredicate, DEFAULT_POLLING_INTERVAL, DEFAULT_POLLING_MAX_TIME);
    }

    public <T> T poll(Callable<T> apiCall, Predicate<T> statusPredicate, long pollingIntervalMillis, long maxPollingTimeMillis) {
        long startTime = System.currentTimeMillis();
        try {
            T result;
            do {
                try {
                    result = apiCall.call();
                } catch (Exception e) {
                    throw new RuntimeException("Error while calling apiCall", e);
                }
                if (statusPredicate.test(result)) {
                    return result;
                }
                Thread.sleep(pollingIntervalMillis);
            } while (System.currentTimeMillis() - startTime < maxPollingTimeMillis);
            throw new RuntimeException("Polling exceeded maximum time");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Polling interrupted", e);
        }
    }
}
