package com.dataset.ipldashboard.service;

import com.dataset.ipldashboard.model.TeamModel;
import com.dataset.ipldashboard.repository.MatchRepository;
import com.dataset.ipldashboard.repository.TeamRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public TeamService(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    public TeamModel getTeam(String teamName){
        TeamModel teamModel = teamRepository.findByTeamName(teamName);
        Pageable pageable = PageRequest.of(0,4);
        teamModel.setMatches(matchRepository.findByTeam1OrTeam2OrderByDateDesc(teamName,teamName,pageable));
        return teamModel;
    }
}
