package com.bank.aggregator.models.solidbank;

import com.bank.aggregator.models.MaritalStatus;
import com.bank.aggregator.models.common.ApplicationRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SbApplicationRequest {
    private String phone;
    private String email;
    private double monthlyIncome;
    private double monthlyExpenses;
    private MaritalStatus maritalStatus;
    private boolean agreeToBeScored;
    private double amount;

    public static SbApplicationRequest from(ApplicationRequest applicationRequest){
        return SbApplicationRequest.builder()
                .phone(applicationRequest.getPhone())
                .email(applicationRequest.getEmail())
                .monthlyIncome(applicationRequest.getMonthlyIncome())
                .monthlyExpenses(applicationRequest.getMonthlyExpenses())
                .maritalStatus(applicationRequest.getMaritalStatus())
                .agreeToBeScored(applicationRequest.isAgreeToDataUsage())
                .amount(applicationRequest.getAmount())
                .build();
    }
}
