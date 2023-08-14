package com.bank.aggregator.models.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Application {
    private ApplicationRequest request;
    private List<Offer> offers;
}
