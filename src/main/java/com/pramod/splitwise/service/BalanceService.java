package com.pramod.splitwise.service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

public interface BalanceService {

    @Transactional
    Map<Long, BigDecimal> computeGroupBalances(Long groupId);

    @Transactional
    void resetGroupBalances(Long groupId);
}
