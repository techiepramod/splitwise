package com.pramod.splitwise.service.impl;

import com.pramod.splitwise.entity.*;
import com.pramod.splitwise.repository.*;
import com.pramod.splitwise.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class SettlementServiceImpl implements SettlementService {
    @Autowired
    private SettlementRepository settlementRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Transactional
    @Override
    public Settlement settle(Long payerId, Long receiverId, Long groupId, BigDecimal amount) {
        User payer = userRepo.findById(payerId).orElseThrow();
        User receiver = userRepo.findById(receiverId).orElseThrow();
        Group group = groupRepo.findById(groupId).orElseThrow();

        // Optional: validate that payer and receiver are in the group
        if (!group.getMembers().contains(payer) || !group.getMembers().contains(receiver)) {
            throw new IllegalArgumentException("Both users must be in the group");
        }

        Settlement settlement = new Settlement();
        settlement.setPayer(payer);
        settlement.setReceiver(receiver);
        settlement.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
        settlement.setGroup(group);
        settlement.setSettledAt(LocalDateTime.now());

        return settlementRepo.save(settlement);
    }
}
