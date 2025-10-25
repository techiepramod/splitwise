package com.pramod.splitwise.service.impl;

import com.pramod.splitwise.entity.*;
import com.pramod.splitwise.repository.*;
import com.pramod.splitwise.service.BalanceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private SettlementRepository settlementRepo;

    @Autowired
    private GroupRepository groupRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Map<Long, BigDecimal> computeGroupBalances(Long groupId) {
        Group group = new Group();
        group.setId(groupId);

        Map<Long, BigDecimal> balances = new HashMap<>();

        // Step 1: Calculate owed amounts from expenses
        List<Expense> expenses = expenseRepo.findByGroup(group);
        for (Expense expense : expenses) {
            for (Map.Entry<Long, BigDecimal> entry : expense.getSplitMap().entrySet()) {
                Long userId = entry.getKey();
                BigDecimal owed = entry.getValue().setScale(2, RoundingMode.HALF_UP);
                balances.put(userId, balances.getOrDefault(userId, BigDecimal.ZERO).subtract(owed));
            }
            Long payerId = expense.getCreatedBy().getId();
            balances.put(payerId, balances.getOrDefault(payerId, BigDecimal.ZERO).add(expense.getAmount()));
        }

        // Step 2: Apply settlements
        List<Settlement> settlements = settlementRepo.findByGroup(group);
        for (Settlement s : settlements) {
            BigDecimal amt = s.getAmount().setScale(2, RoundingMode.HALF_UP);
            balances.put(s.getPayer().getId(), balances.getOrDefault(s.getPayer().getId(), BigDecimal.ZERO).add(amt));
            balances.put(s.getReceiver().getId(), balances.getOrDefault(s.getReceiver().getId(), BigDecimal.ZERO).subtract(amt));
        }

        return balances;
    }

    @Override
    @Transactional
    public void resetGroupBalances(Long groupId) {
        Group group = groupRepo.findById(groupId).orElseThrow();

        // Step 1: Delete all settlements for the group
        settlementRepo.findByGroup(group).forEach(settlementRepo::delete);

        // Step 2: Clear all expense split maps (if you want a full reset)
        expenseRepo.findByGroup(group).forEach(expense -> {
            expense.getSplitMap().clear();
            expenseRepo.save(expense);
        });

        // Step 3: Flush changes to ensure consistency
        entityManager.flush();
    }

}