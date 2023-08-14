package com.bank.aggregator.models.common;

import com.bank.aggregator.models.MaritalStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ApplicationRequest {

    @Pattern(regexp = "\\+371[0-9]{8}", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Positive(message = "Monthly income must be a positive number")
    private double monthlyIncome;

    @PositiveOrZero(message = "Monthly expenses must be a non-negative number")
    private double monthlyExpenses;

    @PositiveOrZero(message = "Monthly credit liabilities must be a non-negative number")
    private double monthlyCreditLiabilities;

    @NotNull(message = "Marital status is required")
    private MaritalStatus maritalStatus;

    @Min(value = 0, message = "Dependents must be a non-negative number")
    private int dependents;
    private boolean agreeToDataUsage;

    @Positive(message = "Amount must be a positive number")
    private double amount;
}
