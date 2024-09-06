package com.depromeet.report.port.out.persistence;

import com.depromeet.report.port.in.command.CreateReportCommand;

public interface ReportPersistencePort {
    void writeReportToSheet(CreateReportCommand command);
}
