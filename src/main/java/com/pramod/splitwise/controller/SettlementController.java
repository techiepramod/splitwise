package com.pramod.splitwise.controller;

import com.pramod.splitwise.entity.Settlement;
import com.pramod.splitwise.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/settlements")
public class SettlementController {
    @Autowired
    private SettlementService settlementService;

    @PostMapping
    public Settlement settle(@RequestParam Long payerId, @RequestParam Long receiverId,
                             @RequestParam Long groupId, @RequestParam BigDecimal amount) {
        return settlementService.settle(payerId, receiverId, groupId, amount);
    }
}

