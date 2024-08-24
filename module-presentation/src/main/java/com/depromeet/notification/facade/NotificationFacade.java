package com.depromeet.notification.facade;

import com.depromeet.exception.InternalServerException;
import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.domain.FollowType;
import com.depromeet.notification.domain.ReactionLog;
import com.depromeet.notification.dto.response.BaseNotificationResponse;
import com.depromeet.notification.dto.response.FollowNotificationResponse;
import com.depromeet.notification.dto.response.FriendNotificationResponse;
import com.depromeet.notification.dto.response.NotificationResponse;
import com.depromeet.notification.dto.response.ReactionNotificationResponse;
import com.depromeet.notification.port.in.usecaase.GetFollowLogUseCase;
import com.depromeet.notification.port.in.usecaase.GetReactionLogUseCase;
import com.depromeet.type.friend.FollowErrorType;
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
}
