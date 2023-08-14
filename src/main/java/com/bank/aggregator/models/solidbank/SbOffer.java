package com.bank.aggregator.models.solidbank;

import com.bank.aggregator.models.common.Offer;
import lombok.Data;

@Data
public class SbOffer {
    private double monthlyPaymentAmount;
    private double totalRepaymentAmount;
    private int numberOfPayments;
    private double annualPercentageRate;
    private String firstRepaymentDate;

    public Offer toOffer() {
        return Offer.builder()
                .source("SolidBank")
                .monthlyPaymentAmount(monthlyPaymentAmount)
                .totalRepaymentAmount(totalRepaymentAmount)
                .numberOfPayments(numberOfPayments)
                .annualPercentageRate(annualPercentageRate)
                .firstRepaymentDate(firstRepaymentDate)
                .build();
    }
}
