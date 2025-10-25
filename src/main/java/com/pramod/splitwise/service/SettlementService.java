package com.pramod.splitwise.service;

import com.pramod.splitwise.entity.Settlement;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface SettlementService {

    @Transactional
    Settlement settle(Long payerId, Long receiverId, Long groupId, BigDecimal amount);
}
