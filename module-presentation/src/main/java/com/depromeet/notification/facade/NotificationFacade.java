package com.depromeet.notification.facade;

import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.domain.FollowType;
import com.depromeet.notification.domain.ReactionLog;
import com.depromeet.notification.dto.request.UpdateReadNotificationRequest;
import com.depromeet.notification.dto.response.BaseNotificationResponse;
import com.depromeet.notification.dto.response.FollowNotificationResponse;
import com.depromeet.notification.dto.response.FriendNotificationResponse;
import com.depromeet.notification.dto.response.NotificationResponse;
import com.depromeet.notification.dto.response.ReactionNotificationResponse;
import com.depromeet.notification.dto.response.UnreadNotificationCountResponse;
import com.depromeet.notification.mapper.NotificationMapper;
import com.depromeet.notification.port.in.usecase.GetFollowLogUseCase;
import com.depromeet.notification.port.in.usecase.GetReactionLogUseCase;
import com.depromeet.notification.port.in.usecase.UpdateFollowLogUseCase;
import com.depromeet.notification.port.in.usecase.UpdateReactionLogUseCase;
import com.depromeet.type.friend.FollowErrorType;
import com.depromeet.type.notification.NotificationErrorType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationFacade {
    private final GetFollowLogUseCase getFollowLogUseCase;
    private final GetReactionLogUseCase getReactionLogUseCase;
    private final UpdateFollowLogUseCase updateFollowLogUseCase;
    private final UpdateReactionLogUseCase updateReactionLogUseCase;

    @Value("${cloud-front.domain}")
    private String profileImageOrigin;

    public NotificationResponse getNotifications(Long memberId, LocalDateTime cursorCreatedAt) {
        List<FollowLog> followLogs = getFollowLogUseCase.getFollowLogs(memberId, cursorCreatedAt);
        List<ReactionLog> reactionLogs =
                getReactionLogUseCase.getReactionsLogs(memberId, cursorCreatedAt);

        List<BaseNotificationResponse> followResponses = getFollowResponses(followLogs);
        List<BaseNotificationResponse> reactionResponses =
                ReactionNotificationResponse.from(reactionLogs);
        followResponses.addAll(reactionResponses);

        followResponses.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        boolean hasNext = followResponses.size() > 10;
        LocalDateTime nextCursorCreatedAt = null;
        if (hasNext) {
            BaseNotificationResponse response = followResponses.removeLast();
            nextCursorCreatedAt = response.getCreatedAt();
        }
        List<BaseNotificationResponse> result =
                followResponses.subList(0, Math.min(followResponses.size(), 10));

        return new NotificationResponse(result, nextCursorCreatedAt, hasNext);
    }

    private List<BaseNotificationResponse> getFollowResponses(List<FollowLog> followLogs) {
        return followLogs.stream()
                .map(
                        log -> {
                            if (log.getType().equals(FollowType.FRIEND)) {
                                return FriendNotificationResponse.from(log, profileImageOrigin);
                            } else if (log.getType().equals(FollowType.FOLLOW)) {
                                return FollowNotificationResponse.from(log, profileImageOrigin);
                            } else {
                                throw new InternalServerException(
                                        FollowErrorType.INVALID_FOLLOW_TYPE);
                            }
                        })
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsReadNotification(Long memberId, UpdateReadNotificationRequest request) {
        if (request.type().equals("CHEER")) {
            updateReactionLogUseCase.markAsReadReactionLog(memberId, request.notificationId());
        } else if (request.type().equals("FOLLOW") || request.type().equals("FRIEND")) {
            updateFollowLogUseCase.markAsReadFollowLog(
                    memberId, NotificationMapper.toCommand(request));
        } else {
            throw new BadRequestException(NotificationErrorType.INVALID_NOTIFICATION_TYPE);
        }
    }

    public UnreadNotificationCountResponse getUnreadNotificationCount(Long memberId) {
        Long followCount = getFollowLogUseCase.getUnreadFollowLogCount(memberId);
        Long reactionCount = getReactionLogUseCase.getUnreadReactionLogCount(memberId);
        return new UnreadNotificationCountResponse(followCount + reactionCount);
    }
}
