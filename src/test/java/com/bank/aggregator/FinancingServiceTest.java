package com.bank.aggregator;

import com.bank.aggregator.models.common.Application;
import com.bank.aggregator.models.common.ApplicationRequest;
import com.bank.aggregator.models.common.Offer;
import com.bank.aggregator.services.BankService;
import com.bank.aggregator.services.FinancingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancingServiceTest {

    @Mock
    private BankService bankService1;

    @Mock
    private BankService bankService2;

    @Mock
    private ApplicationRequest applicationRequest;

    private FinancingService financingService;

    @BeforeEach
    void setUp() {
        financingService = new FinancingService(List.of(bankService1, bankService2));
    }

    @Test
    void apply_ReturnsApplicationWithOffers_WhenAllBankServicesSucceed() {
        when(bankService1.submitApplication(applicationRequest)).thenReturn(Optional.of(new Offer()));
        when(bankService2.submitApplication(applicationRequest)).thenReturn(Optional.of(new Offer()));

        Application result = financingService.apply(applicationRequest);

        assertEquals(2, result.getOffers().size());
        verify(bankService1).submitApplication(applicationRequest);
        verify(bankService2).submitApplication(applicationRequest);
    }

    @Test
    void apply_ReturnsApplicationWithoutOffers_WhenAllBankServicesFail() {
        when(bankService1.submitApplication(applicationRequest)).thenReturn(Optional.empty());
        when(bankService2.submitApplication(applicationRequest)).thenReturn(Optional.empty());

        Application result = financingService.apply(applicationRequest);

        assertEquals(0, result.getOffers().size());
        verify(bankService1).submitApplication(applicationRequest);
        verify(bankService2).submitApplication(applicationRequest);
    }

    @Test
    void apply_ReturnsApplicationWithMixedOffers_WhenSomeBankServicesSucceed() {
        when(bankService1.submitApplication(applicationRequest)).thenReturn(Optional.of(new Offer()));
        when(bankService2.submitApplication(applicationRequest)).thenReturn(Optional.empty());

        Application result = financingService.apply(applicationRequest);

        assertEquals(1, result.getOffers().size());
        verify(bankService1).submitApplication(applicationRequest);
        verify(bankService2).submitApplication(applicationRequest);
    }

    @Test
    void apply_ThrowsException_WhenBankServiceThrowsException() {
        when(bankService1.submitApplication(applicationRequest)).thenThrow(new RuntimeException("Bank service error"));
        when(bankService2.submitApplication(applicationRequest)).thenReturn(Optional.of(new Offer()));

        assertThrows(RuntimeException.class, () -> financingService.apply(applicationRequest));

        verify(bankService1).submitApplication(applicationRequest);
        verify(bankService2).submitApplication(applicationRequest);
    }

    @Test
    void apply_ReturnsApplicationWithoutOffers_WhenAllBankServicesReturnEmptyOffers() {
        when(bankService1.submitApplication(applicationRequest)).thenReturn(Optional.empty());
        when(bankService2.submitApplication(applicationRequest)).thenReturn(Optional.empty());

        Application result = financingService.apply(applicationRequest);

        assertEquals(0, result.getOffers().size());
        verify(bankService1).submitApplication(applicationRequest);
        verify(bankService2).submitApplication(applicationRequest);
    }
}

