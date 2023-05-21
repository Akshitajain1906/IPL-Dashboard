package com.dataset.ipldashboard.repository;

import com.dataset.ipldashboard.model.MatchModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface MatchRepository extends JpaRepository<MatchModel,Long> {
    List<MatchModel> findByTeam1OrTeam2OrderByDateDesc(String team1, String team2, Pageable pageable);
}
