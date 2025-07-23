package com.epam.customaijira.client;

import com.epam.customaijira.model.DialRequest;
import com.epam.customaijira.model.DialResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "dialClient",
    url = "${dial.endpoint}",
    configuration = com.epam.customaijira.config.FeignConfig.class
)
public interface DialClient {
    @PostMapping("/chat/completions?api-version=2023-05-15")
    DialResponse summarize(@RequestBody DialRequest request);
} 