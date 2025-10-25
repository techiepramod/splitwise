package com.pramod.splitwise.service.impl;

import com.pramod.splitwise.entity.*;
import com.pramod.splitwise.repository.*;
import com.pramod.splitwise.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityNotFoundException;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    /**
     * Adds a new expense to a group with a custom split map.
     */
    @Override
    @Transactional
    public Expense addExpense(Long groupId, Long creatorId, BigDecimal amount, String description, Map<Long, BigDecimal> splitMap) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        User creator = userRepo.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!group.getMembers().contains(creator)) {
            throw new IllegalArgumentException("User is not a member of the group");
        }

        // Validate splitMap: all users must be in the group
        for (Long userId : splitMap.keySet()) {
            if (group.getMembers().stream().noneMatch(u -> u.getId().equals(userId))) {
                throw new IllegalArgumentException("User " + userId + " is not in the group");
            }
        }

        // Normalize amounts
        Map<Long, BigDecimal> normalizedSplit = new HashMap<>();
        for (Map.Entry<Long, BigDecimal> entry : splitMap.entrySet()) {
            normalizedSplit.put(entry.getKey(), entry.getValue().setScale(2, RoundingMode.HALF_UP));
        }

        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setCreatedBy(creator);
        expense.setAmount(amount.setScale(2, RoundingMode.HALF_UP));
        expense.setDescription(description);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setSplitMap(normalizedSplit);

        return expenseRepo.save(expense);
    }

    /**
     * Returns all expenses for a group, only if the user is a member.
     */
    @Override
    public List<Expense> getExpensesForGroup(Long groupId, String userEmail) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        boolean isMember = group.getMembers().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(userEmail));

        if (!isMember) {
            throw new SecurityException("Access denied: user is not a member of this group");
        }

        return expenseRepo.findByGroup(group);
    }
}
