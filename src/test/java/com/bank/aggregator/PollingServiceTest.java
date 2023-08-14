package com.bank.aggregator;

import com.bank.aggregator.services.PollingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Callable;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollingServiceTest {

    @Mock
    private Callable<String> mockApiCall;

    @Mock
    private Predicate<String> mockStatusPredicate;

    private PollingService pollingService;

    @BeforeEach
    void setUp() {
        pollingService = new PollingService();
    }

    @Test
    void poll_ReturnsResult_WhenStatusPredicateIsTrue() throws Exception {
        when(mockApiCall.call()).thenReturn("success");
        when(mockStatusPredicate.test(any())).thenReturn(true);

        String result = pollingService.poll(mockApiCall, mockStatusPredicate);

        assertEquals("success", result);
        verify(mockApiCall, atLeastOnce()).call();
        verify(mockStatusPredicate, atLeastOnce()).test("success");
    }

    @Test
    void poll_ThrowsException_WhenMaxPollingTimeExceeded() throws Exception {
        when(mockApiCall.call()).thenReturn("incomplete");
        when(mockStatusPredicate.test(any())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> pollingService.poll(mockApiCall, mockStatusPredicate, 500, 1000));

        verify(mockApiCall, atLeast(2)).call();
        verify(mockStatusPredicate, atLeast(2)).test("incomplete");
    }

    @Test
    void poll_ThrowsException_WhenApiCallThrowsException() throws Exception {
        when(mockApiCall.call()).thenThrow(new RuntimeException("API call failed"));

        assertThrows(RuntimeException.class, () -> pollingService.poll(mockApiCall, mockStatusPredicate));
        verify(mockApiCall, atLeastOnce()).call();
        verifyNoMoreInteractions(mockStatusPredicate);
    }
}
