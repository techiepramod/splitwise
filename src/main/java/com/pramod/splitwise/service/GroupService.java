package com.pramod.splitwise.service;

import com.pramod.splitwise.entity.Group;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GroupService {

    Group createGroup(Group group);

    @Transactional
    void addUserToGroup(Long groupId, Long userId);

    List<Group> getAllGroups();

    @Transactional
    void removeUserFromGroup(Long groupId, Long userId);

    boolean isUserInGroup(Long groupId, String email);
}
