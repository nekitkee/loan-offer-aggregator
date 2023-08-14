package com.bank.aggregator.models.fastbank;

import com.bank.aggregator.models.common.ApplicationRequest;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FbApplicationRequest {
    private String phoneNumber;
    private String email;
    private double monthlyIncomeAmount;
    private double monthlyCreditLiabilities;
    private int dependents;
    private boolean agreeToDataSharing;
    private double amount;

    //NOTE : If API DTO conversion involves simple property mapping without much additional logic,
    //using Converters (Mappers) might introduce unnecessary complexity.

    public static FbApplicationRequest from(ApplicationRequest applicationRequest){
        return FbApplicationRequest.builder()
                .phoneNumber(applicationRequest.getPhone())
                .email(applicationRequest.getEmail())
                .monthlyIncomeAmount(applicationRequest.getMonthlyIncome())
                .monthlyCreditLiabilities(applicationRequest.getMonthlyCreditLiabilities())
                .dependents(applicationRequest.getDependents())
                .agreeToDataSharing(applicationRequest.isAgreeToDataUsage())
                .amount(applicationRequest.getAmount())
                .build();
    }
}
