package com.epam.customaijira.controller;

import com.epam.customaijira.service.TicketSummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TicketController {

    private final TicketSummaryService ticketSummaryService;

    public TicketController(TicketSummaryService ticketSummaryService) {
        this.ticketSummaryService = ticketSummaryService;
    }

    @GetMapping("/tickets/summaries")
    public List<Map<String, String>> getTicketSummaries() {
        return ticketSummaryService.getTicketSummaries();
    }
} 