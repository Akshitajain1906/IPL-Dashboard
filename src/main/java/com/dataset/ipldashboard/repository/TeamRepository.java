package com.dataset.ipldashboard.repository;

import com.dataset.ipldashboard.model.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamModel,Integer> {
    TeamModel findByTeamName(String teamName);
}
