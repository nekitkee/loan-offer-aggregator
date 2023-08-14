package com.bank.aggregator.clients;

import com.bank.aggregator.models.solidbank.SbApplication;
import com.bank.aggregator.models.solidbank.SbApplicationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "solidBank", url = "${solidbank.api.url}")
public interface SolidBankClient {
    @PostMapping("/applications")
    SbApplication submitApplication(SbApplicationRequest applicationRequest);

    @GetMapping("/applications/{id}")
    SbApplication getApplicationById(@PathVariable("id") String id);
}
