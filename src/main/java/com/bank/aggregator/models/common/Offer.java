package com.bank.aggregator.models.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    private String source;
    private double monthlyPaymentAmount;
    private double totalRepaymentAmount;
    private int numberOfPayments;
    private double annualPercentageRate;
    private String firstRepaymentDate;
}
