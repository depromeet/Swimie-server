package com.depromeet.memory.port.in.command;

public record UpdateStrokeCommand(Long id, String name, Float laps, Integer meter) {}
