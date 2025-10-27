package com.pramod.splitwise.controller;

import com.pramod.splitwise.service.BalanceService;
import com.pramod.splitwise.entity.Group;
import com.pramod.splitwise.repository.ExpenseRepository;
import com.pramod.splitwise.repository.GroupRepository;
import com.pramod.splitwise.repository.SettlementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private SettlementRepository settlementRepo;

    @Autowired
    private BalanceService balanceService;

    /**
     * Admin-only: Reset all balances in a group.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/groups/{groupId}/reset")
    public void resetBalances(@PathVariable Long groupId) {
        balanceService.resetGroupBalances(groupId);
    }

    /**
     * Admin-only: Delete a group after verifying no unsettled balances.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @DeleteMapping("/groups/{groupId}")
    public void deleteGroupSafely(@PathVariable Long groupId) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        Map<Long, BigDecimal> balances = balanceService.computeGroupBalances(groupId);
        boolean hasUnsettled = balances.values().stream()
                .anyMatch(b -> b.compareTo(BigDecimal.ZERO) != 0);

        if (hasUnsettled) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete group with unsettled balances");
        }

        // Delete settlements
        settlementRepo.deleteAll(settlementRepo.findByGroup(group));

        // Delete expenses
        expenseRepo.deleteAll(expenseRepo.findByGroup(group));

        // Delete group
        groupRepo.delete(group);
    }
}