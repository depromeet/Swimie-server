package com.depromeet.report.service;

import com.depromeet.report.port.in.command.CreateReportCommand;
import com.depromeet.report.port.in.usecase.CreateReportUseCase;
import com.depromeet.report.port.out.persistence.ReportPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!batch")
@RequiredArgsConstructor
public class ReportService implements CreateReportUseCase {
    private final ReportPersistencePort reportPersistencePort;

    @Override
    public void save(CreateReportCommand command) {
        reportPersistencePort.writeReportToSheet(command);
    }
}
