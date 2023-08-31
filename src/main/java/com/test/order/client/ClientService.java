package com.test.order.client;

import com.test.order.config.ClientServiceConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientService", url = "http://localhost:8001", configuration = ClientServiceConfiguration.class)
public interface ClientService {

    @GetMapping("/api/client/{clientId}/exist")
    Boolean checkIfClientExists(@PathVariable Long clientId);
}
