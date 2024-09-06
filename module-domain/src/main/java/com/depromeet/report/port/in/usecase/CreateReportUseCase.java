package com.depromeet.report.port.in.usecase;

import com.depromeet.report.port.in.command.CreateReportCommand;

public interface CreateReportUseCase {
    void save(CreateReportCommand command);
}
