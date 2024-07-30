package com.depromeet.memory.port.in.query;

import java.time.LocalDate;
import java.time.YearMonth;

public record TimelineQuery(LocalDate cursorRecordAt, YearMonth date, boolean showNewer) {}
