package com.depromeet.withdrawalReason.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.withdrawal.domain.WithdrawalReason;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalReasonEntity extends BaseTimeEntity {
    @Id
    @Column(name = "withdrawal_reason_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PersistenceReasonType reason;

    @Column(length = 1000)
    private String feedback;

    @Builder
    public WithdrawalReasonEntity(Long id, PersistenceReasonType reason, String feedback) {
        this.id = id;
        this.reason = reason;
        this.feedback = feedback;
    }

    public static WithdrawalReasonEntity from(WithdrawalReason withdrawalReason) {
        return WithdrawalReasonEntity.builder()
                .id(withdrawalReason.getId())
                .reason(PersistenceReasonType.from(withdrawalReason.getReason()))
                .feedback(withdrawalReason.getFeedback())
                .build();
    }
}
