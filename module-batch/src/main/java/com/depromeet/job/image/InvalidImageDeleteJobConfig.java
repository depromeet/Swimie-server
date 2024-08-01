package com.depromeet.job.image;

import com.depromeet.image.entity.ImageEntity;
import com.depromeet.job.image.listener.ImageDeleteJobExecutionListener;
import com.depromeet.job.image.processor.ImageItemProcessor;
import com.depromeet.job.image.reader.ImageItemReader;
import com.depromeet.job.image.writer.ImageItemWriter;
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
public class InvalidImageDeleteJobConfig {
    private final ImageItemReader itemReader;
    private final ImageItemWriter itemWriter;
    private final ImageItemProcessor itemProcessor;
    private final ImageDeleteJobExecutionListener itemListener;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job invalidImageDeleteJob(JobRepository jobRepository) throws Exception {
        return new JobBuilder("invalidImageDeleteJob", jobRepository)
                .listener(itemListener)
                .start(invalidImageDeleteStep(jobRepository))
                .build();
    }

    @Bean
    @JobScope
    public Step invalidImageDeleteStep(JobRepository jobRepository) {
        return new StepBuilder("invalidImageDeleteStep", jobRepository)
                .<ImageEntity, ImageEntity>chunk(10, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .allowStartIfComplete(true)
                .build();
    }
}
