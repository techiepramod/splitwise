package com.pramod.splitwise.service;

import com.pramod.splitwise.entity.Expense;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ExpenseService {

    @Transactional
    Expense addExpense(Long groupId, Long creatorId, BigDecimal amount, String description, Map<Long, BigDecimal> splitMap);

    List<Expense> getExpensesForGroup(Long groupId, String userEmail);
}
