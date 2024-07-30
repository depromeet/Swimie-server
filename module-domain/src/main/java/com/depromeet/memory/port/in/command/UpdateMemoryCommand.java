package com.depromeet.memory.port.in.command;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

public record UpdateMemoryCommand(
        Long poolId,
        String item,
        Short heartRate,
        LocalTime pace,
        Integer kcal,
        LocalDate recordAt,
        LocalTime startTime,
        LocalTime endTime,
        Short lane,
        String diary,
        List<UpdateStrokeCommand> strokes) {
    @Builder
    public UpdateMemoryCommand {}
}
