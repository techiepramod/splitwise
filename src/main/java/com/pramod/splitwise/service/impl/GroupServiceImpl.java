package com.pramod.splitwise.service.impl;

import com.pramod.splitwise.entity.*;
import com.pramod.splitwise.repository.*;
import com.pramod.splitwise.service.BalanceService;
import com.pramod.splitwise.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    BalanceService balanceService;

    @Override
    public Group createGroup(Group group) {
        return groupRepo.save(group);
    }

    @Override
    @Transactional
    public void addUserToGroup(Long groupId, Long userId) {
        Group group = groupRepo.findById(groupId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();
        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
            groupRepo.save(group);
        }
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepo.findAll();
    }

    @Override
    @Transactional
    public void removeUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepo.findById(groupId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();

        // Check unsettled balances
        Map<Long, BigDecimal> balances = balanceService.computeGroupBalances(groupId);
        BigDecimal netBalance = balances.getOrDefault(userId, BigDecimal.ZERO);

        if (netBalance.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("User has unsettled balance: " + netBalance);
        }

        group.getMembers().removeIf(u -> u.getId().equals(userId));
        groupRepo.save(group);
    }

    @Override
    public boolean isUserInGroup(Long groupId, String email) {
        Group group = groupRepo.findById(groupId).orElseThrow();
        return group.getMembers().stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
