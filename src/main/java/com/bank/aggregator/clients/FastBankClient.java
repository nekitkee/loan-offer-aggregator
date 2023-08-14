package com.bank.aggregator.clients;

import com.bank.aggregator.models.fastbank.FbApplication;
import com.bank.aggregator.models.fastbank.FbApplicationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "fastBank", url = "${fastbank.api.url}")
public interface FastBankClient {
    @PostMapping("/applications")
    FbApplication submitApplication(FbApplicationRequest applicationRequest);

    @GetMapping("/applications/{id}")
    FbApplication getApplicationById(@PathVariable("id") String id);
}
