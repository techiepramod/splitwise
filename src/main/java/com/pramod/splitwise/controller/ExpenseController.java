package com.pramod.splitwise.controller;

import com.pramod.splitwise.entity.Expense;
import com.pramod.splitwise.service.ExpenseService;
import com.pramod.splitwise.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private GroupService groupService;

    @PostMapping
    public Expense addExpense(@RequestParam Long groupId, @RequestParam Long creatorId,
                              @RequestParam BigDecimal amount, @RequestParam String description,
                              @RequestBody Map<Long, BigDecimal> splitMap) {
        return expenseService.addExpense(groupId, creatorId, amount, description, splitMap);
    }

    @GetMapping("/group/{groupId}")
    public List<Expense> getGroupExpenses(@PathVariable Long groupId, @RequestParam String email) {
        if (!groupService.isUserInGroup(groupId, email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return expenseService.getExpensesForGroup(groupId,email);
    }
}
