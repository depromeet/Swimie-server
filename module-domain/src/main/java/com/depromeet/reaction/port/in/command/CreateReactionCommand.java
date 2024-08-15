package com.depromeet.reaction.port.in.command;

public record CreateReactionCommand(Long memoryId, String emoji, String comment) {}
