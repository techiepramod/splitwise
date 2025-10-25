package com.pramod.splitwise.repository;

import com.pramod.splitwise.entity.Group;
import com.pramod.splitwise.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByGroup(Group group);
}

