package com.pramod.splitwise.repository;

import com.pramod.splitwise.entity.Expense;
import com.pramod.splitwise.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroup(Group group);
}


