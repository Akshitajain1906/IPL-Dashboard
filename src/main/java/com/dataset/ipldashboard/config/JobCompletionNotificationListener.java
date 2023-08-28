package com.dataset.ipldashboard.config;

import com.dataset.ipldashboard.model.TeamModel;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

//    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;

    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate, EntityManager em) {
        this.em = em;
//        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
//
////            jdbcTemplate.query("SELECT id,team1,team2 FROM match",
////                    (rs, row) -> "id:" + rs.getString(1)+", team1:"+ rs.getString(2)
////                            +", team2:"+ rs.getString(3)
////            ).forEach(match -> {log.info(match);
////                                System.out.println(match);
////                               });
            Map<String,TeamModel> teamData = new HashMap<>();
//
            em.createQuery("select distinct m.team1, count(*) from match m group by m.team1",Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new TeamModel((String)e[0],(long)e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(),team));

            em.createQuery("select distinct m.team2, count(*) from match m group by m.team2",Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e->{
                        TeamModel teamModel = teamData.get((String)e[0]);
                        teamModel.setTotalMatches(teamModel.getTotalMatches()+(long)e[1]);
//                        teamData.replace((String)e[0],teamModel);
                    });
            em.createQuery("select distinct m.matchWinner, count(*) from match m group by m.matchWinner",Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e->{
                        TeamModel teamModel = teamData.get((String)e[0]);
                        if (teamModel!=null) {
                            teamModel.setTotalWins((long) e[1]);
//                            teamData.replace((String) e[0], teamModel);
                        }
                    });
             teamData.values().forEach(em::persist);
            teamData.values().forEach(System.out::println);
        }
    }
}