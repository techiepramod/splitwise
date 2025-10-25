package com.pramod.splitwise;

import com.pramod.splitwise.entity.Expense;
import com.pramod.splitwise.entity.Group;
import com.pramod.splitwise.entity.Role;
import com.pramod.splitwise.entity.User;
import com.pramod.splitwise.repository.ExpenseRepository;
import com.pramod.splitwise.repository.GroupRepository;
import com.pramod.splitwise.repository.UserRepository;
import com.pramod.splitwise.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ExpenseServiceTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    @Test
    void shouldAddExpenseWithValidSplit() {
        User u1 = userRepo.save(new User("A", "a@gmail.com", "oauth1", Role.USER));
        User u2 = userRepo.save(new User("B", "b@gmail.com", "oauth2", Role.USER));
        Group group = groupRepo.save(new Group("Lunch"));
        group.getMembers().addAll(List.of(u1, u2));
        groupRepo.save(group);

        Map<Long, BigDecimal> split = Map.of(
                u1.getId(), new BigDecimal("50.00"),
                u2.getId(), new BigDecimal("50.00")
        );

        Expense expense = expenseService.addExpense(group.getId(), u1.getId(), new BigDecimal("100.00"), "Pizza", split);

        assertEquals("Pizza", expense.getDescription());
        assertEquals(2, expense.getSplitMap().size());
    }

    @Test
    void shouldRejectSplitWithNonMember() {
        User u1 = userRepo.save(new User("A", "a@gmail.com", "oauth1", Role.USER));
        User outsider = userRepo.save(new User("X", "x@gmail.com", "oauthX", Role.USER));
        Group group = groupRepo.save(new Group("Secret"));
        group.getMembers().add(u1);
        groupRepo.save(group);

        Map<Long, BigDecimal> split = Map.of(
                u1.getId(), new BigDecimal("50.00"),
                outsider.getId(), new BigDecimal("50.00")
        );

        assertThrows(IllegalArgumentException.class, () ->
                expenseService.addExpense(group.getId(), u1.getId(), new BigDecimal("100.00"), "Oops", split)
        );
    }
}