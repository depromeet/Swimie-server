package com.depromeet.job.followinglog;

import com.depromeet.followinglog.entity.FollowingMemoryLogEntity;
import com.depromeet.job.followinglog.listener.FollowingLogDeleteJobExecutionListener;
import com.depromeet.job.followinglog.reader.FollowingLogItemReader;
import com.depromeet.job.followinglog.writer.FollowingLogItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FollowingLogDeleteJobConfig {
    private final PlatformTransactionManager transactionManager;
    private final FollowingLogDeleteJobExecutionListener listener;
    private final FollowingLogItemReader itemReader;
    private final FollowingLogItemWriter itemWriter;

    @Bean
    public Job followingLogDeleteJob(JobRepository jobRepository) {
        return new JobBuilder("followingLogDeleteJob", jobRepository)
                .listener(listener)
                .start(followingLogDeleteStep(jobRepository))
                .build();
    }

    @Bean
    @JobScope
    public Step followingLogDeleteStep(JobRepository jobRepository) {
        return new StepBuilder("followingLogDeleteStep", jobRepository)
                .<FollowingMemoryLogEntity, FollowingMemoryLogEntity>chunk(10, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .allowStartIfComplete(true)
                .build();
    }
}
