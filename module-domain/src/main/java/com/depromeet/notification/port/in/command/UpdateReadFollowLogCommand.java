package com.depromeet.notification.port.in.command;

import com.depromeet.notification.domain.FollowType;

public record UpdateReadFollowLogCommand(Long followLogId, FollowType type) {}
