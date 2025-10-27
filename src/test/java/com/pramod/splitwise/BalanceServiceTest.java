package com.pramod.splitwise;

import com.pramod.splitwise.entity.Group;
import com.pramod.splitwise.entity.Role;
import com.pramod.splitwise.entity.User;
import com.pramod.splitwise.repository.GroupRepository;
import com.pramod.splitwise.repository.UserRepository;
import com.pramod.splitwise.service.BalanceService;
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
class BalanceServiceTest {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Test
    void shouldComputeCorrectBalances() {
        User u1 = userRepo.save(new User("A", "a@gmail.com", "oauth1", Role.USER));
        User u2 = userRepo.save(new User("B", "b@gmail.com", "oauth2", Role.USER));
        Group group = groupRepo.save(new Group("Trip"));
        group.getMembers().addAll(List.of(u1, u2));
        groupRepo.save(group);

        Map<Long, BigDecimal> split = Map.of(
                u1.getId(), new BigDecimal("30.00"),
                u2.getId(), new BigDecimal("70.00")
        );

        expenseService.addExpense(group.getId(), u1.getId(), new BigDecimal("100.00"), "Taxi", split);

        Map<Long, BigDecimal> balances = balanceService.computeGroupBalances(group.getId());

        assertEquals(new BigDecimal("70.00"), balances.get(u1.getId()));
        assertEquals(new BigDecimal("-70.00"), balances.get(u2.getId()));
    }
}