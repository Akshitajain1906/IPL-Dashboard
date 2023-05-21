package com.dataset.ipldashboard.config;

import com.dataset.ipldashboard.DTO.MatchInputDTO;
import com.dataset.ipldashboard.model.MatchModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
//@EnableBatchProcessing donot enable as it will overwrite auto configure and batch will not be invoked
public class BatchConfig {
    final String[] fieldNames = new String[] {"match_id","season","date","city","venue","team1","team2","toss_winner","toss_decision","player_of_match","winner","winner_wickets","winner_runs","outcome","result_type","results","gender","event","match_number","umpire1","umpire2","reserve_umpire","tv_umpire","match_referee","eliminator","method","date_1"};
    @Bean
    public FlatFileItemReader<MatchInputDTO> reader() {
        return new FlatFileItemReaderBuilder<MatchInputDTO>()
                .name("MatchItemReader")
                .resource(new ClassPathResource("match-data.csv"))
                .delimited()
                .names(fieldNames)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<MatchInputDTO>() {{
                    setTargetType(MatchInputDTO.class);
                }})
                .build();
    }

    @Bean
    public MatchDataProcessor processor() {
        return new MatchDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<MatchModel> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MatchModel>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO match (id, city, date, player_of_match, venue, team1, team2, toss_winner, toss_decision, match_winner, result, umpire1, umpire2) "
                        +"VALUES (:id, :city, :date, :playerOfMatch, :venue, :team1, :team2, :tossWinner, :tossDecision, :matchWinner, :result, :umpire1, :umpire2)")
                .dataSource(dataSource)
                .build();
    }

    //creating a job that does above three steps
    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, Step step1) {
        Job res =
                new JobBuilder("importUserJob", jobRepository)
                        .incrementer(new RunIdIncrementer())
                        .listener(listener)
                        .flow(step1)
                        .end()
                        .build();
        return res;
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager, JdbcBatchItemWriter<MatchModel> writer) {
        return new StepBuilder("step1", jobRepository)
                .<MatchInputDTO, MatchModel> chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}

