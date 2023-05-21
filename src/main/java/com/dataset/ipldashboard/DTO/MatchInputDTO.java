package com.dataset.ipldashboard.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MatchInputDTO {
    @JsonProperty("match_id")
    private String matchId;
    private String season;
    private String date;
    private String city;
    private String venue;
    private String team1;
    private String team2;
    private String toss_winner;
    private String toss_decision;
    private String player_of_match;
    private String winner;
    private String winner_wickets;
    private String winner_runs;
    private String outcome;
    private String result_type;
    private String results;
    private String gender;
    private String event;
    private String match_number;
    private String umpire1;
    private String umpire2;
    private String reserve_umpire;
    private String tv_umpire;
    private String match_referee;
    private String eliminator;
    private String method;
    private String date_1;
}
