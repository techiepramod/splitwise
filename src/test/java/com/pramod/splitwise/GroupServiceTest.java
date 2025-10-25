package com.pramod.splitwise;

import com.pramod.splitwise.entity.Group;
import com.pramod.splitwise.entity.Role;
import com.pramod.splitwise.entity.User;
import com.pramod.splitwise.repository.GroupRepository;
import com.pramod.splitwise.repository.UserRepository;
import com.pramod.splitwise.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class GroupServiceTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Test
    void shouldAddUserToGroup() {
        User user = userRepo.save(new User("A", "a@gmail.com", "oauth123", Role.USER));
        Group group = groupRepo.save(new Group("Trip"));

        groupService.addUserToGroup(group.getId(), user.getId());

        Group updated = groupRepo.findById(group.getId()).orElseThrow();
        assertTrue(updated.getMembers().contains(user));
    }

    @Test
    void shouldNotAddSameUserTwice() {
        User user = userRepo.save(new User("B", "b@gmail.com", "oauth456", Role.USER));
        Group group = groupRepo.save(new Group("Dinner"));

        groupService.addUserToGroup(group.getId(), user.getId());
        groupService.addUserToGroup(group.getId(), user.getId());

        Group updated = groupRepo.findById(group.getId()).orElseThrow();
        assertEquals(1, updated.getMembers().stream().filter(u -> u.getId().equals(user.getId())).count());
    }
}
