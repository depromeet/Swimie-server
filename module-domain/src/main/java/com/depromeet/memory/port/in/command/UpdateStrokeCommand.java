package com.depromeet.memory.port.in.command;

public record UpdateStrokeCommand(Long id, String name, Short laps, Integer meter) {}
