package com.depromeet.job.followinglog.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowingLogDeleteJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        log.info("100일 이후의 FollowingMemoryLog 삭제 Batch Job 시작");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("100일 이후의 FollowingMemoryLog 삭제 Batch Job이 완료되었습니다");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("100일 이후의 FollowingMemoryLog 삭제 Batch Job이 실패하였습니다");
        } else {
            log.info(
                    "100일 이후의 FollowingMemoryLog 삭제 Batch Job 종료 Status : {}",
                    jobExecution.getStatus());
        }
    }
}
