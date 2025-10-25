package com.pramod.splitwise.controller;

import com.pramod.splitwise.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/balances")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/group/{groupId}")
    public Map<Long, BigDecimal> getGroupBalances(@PathVariable Long groupId) {
        return balanceService.computeGroupBalances(groupId);
    }
}
