package com.dataset.ipldashboard.controller;

import com.dataset.ipldashboard.model.TeamModel;
import com.dataset.ipldashboard.service.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/teams/{teamName}")
    public TeamModel getTeam(@PathVariable String teamName){
        return teamService.getTeam(teamName);
    }
}
