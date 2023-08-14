package com.bank.aggregator;

import com.bank.aggregator.clients.FastBankClient;
import com.bank.aggregator.models.ApplicationStatus;
import com.bank.aggregator.models.MaritalStatus;
import com.bank.aggregator.models.common.ApplicationRequest;
import com.bank.aggregator.models.common.Offer;
import com.bank.aggregator.models.fastbank.FbApplication;
import com.bank.aggregator.models.fastbank.FbApplicationRequest;
import com.bank.aggregator.models.fastbank.FbOffer;
import com.bank.aggregator.services.FastBankService;
import com.bank.aggregator.services.PollingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FastBankServiceTest {

    @InjectMocks
    private FastBankService fastBankService;

    @Mock
    private FastBankClient fastBankClient;

    @Mock
    private PollingService pollingService;

    @Test
    void submitApplication_ReturnsOffer_WhenApplicationIsProcessed(){
        ApplicationRequest request = createTestApplicationRequest();
        FbApplication fbApplication = new FbApplication();
        fbApplication.setId("123");
        when(fastBankClient.submitApplication(any())).thenReturn(fbApplication);
        FbApplication processedApplication = new FbApplication();
        FbOffer fbOffer = new FbOffer();
        fbOffer.setMonthlyPaymentAmount(100);
        processedApplication.setOffer(Optional.of(fbOffer));
        processedApplication.setStatus(ApplicationStatus.PROCESSED);

        when(pollingService.poll(any(), any()))
                .thenReturn(processedApplication);

        Optional<Offer> offer = fastBankService.submitApplication(request);

        assertTrue(offer.isPresent());
    }


    @Test
    public void submitApplication_ThrowsException_WhenPollingFails(){
        ApplicationRequest applicationRequest = createTestApplicationRequest();
        FbApplication fbApplication = new FbApplication();
        when(fastBankClient.submitApplication(any(FbApplicationRequest.class))).thenReturn(fbApplication);
        when(pollingService.poll(any(), any())).thenThrow(new RuntimeException("Polling failed"));

        try {
            fastBankService.submitApplication(applicationRequest);
            fail("Expected exception was not thrown");
        } catch (RuntimeException e) {
            assertEquals("Polling failed", e.getMessage());
        }

        verify(fastBankClient, times(1)).submitApplication(any(FbApplicationRequest.class));
        verify(pollingService,times(1)).poll(any(), any());
        verifyNoMoreInteractions(fastBankClient, pollingService);
    }

    private ApplicationRequest createTestApplicationRequest() {
        ApplicationRequest request = new ApplicationRequest();
        request.setPhone("+37126677783");
        request.setEmail("nick.wynther@inbox.lv");
        request.setMonthlyIncome(5000.0);
        request.setMonthlyExpenses(3000.0);
        request.setMaritalStatus(MaritalStatus.SINGLE);
        request.setDependents(2);
        request.setAgreeToDataUsage(true);
        request.setAmount(100000.0);
        return request;

    }
}
