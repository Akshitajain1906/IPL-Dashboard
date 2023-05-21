package com.dataset.ipldashboard.config;

import com.dataset.ipldashboard.DTO.MatchInputDTO;
import com.dataset.ipldashboard.model.MatchModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MatchDataProcessor implements ItemProcessor<MatchInputDTO, MatchModel> {

        private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public MatchModel process(final MatchInputDTO matchInputDTO) throws Exception {

        MatchModel matchModel = new MatchModel();
        matchModel.setId(Long.parseLong(matchInputDTO.getMatchId()));
        matchModel.setCity(matchInputDTO.getCity());

        matchModel.setDate(LocalDate.parse(matchInputDTO.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        matchModel.setPlayerOfMatch(matchInputDTO.getPlayer_of_match());
        matchModel.setVenue(matchInputDTO.getVenue());

        // Set Team 1 and Team 2 depending on the innings order
        String firstInningsTeam, secondInningsTeam;

        if ("bat".equals(matchInputDTO.getToss_decision())) {
            firstInningsTeam = matchInputDTO.getToss_winner();
            secondInningsTeam = matchInputDTO.getToss_winner().equals(matchInputDTO.getTeam1())
                    ? matchInputDTO.getTeam2() : matchInputDTO.getTeam1();

        } else {
            secondInningsTeam = matchInputDTO.getToss_winner();
            firstInningsTeam = matchInputDTO.getToss_winner().equals(matchInputDTO.getTeam1())
                    ? matchInputDTO.getTeam2() : matchInputDTO.getTeam1();
        }
        matchModel.setTeam1(firstInningsTeam);
        matchModel.setTeam2(secondInningsTeam);

        matchModel.setTossWinner(matchInputDTO.getToss_winner());
        matchModel.setTossDecision(matchInputDTO.getToss_decision());
        matchModel.setMatchWinner(matchInputDTO.getWinner());
        matchModel.setResult(matchInputDTO.getResults());
//        match.setResultMargin(matchInput.getResult_margin());
        matchModel.setUmpire1(matchInputDTO.getUmpire1());
        matchModel.setUmpire2(matchInputDTO.getUmpire2());

        return matchModel;
    }

}