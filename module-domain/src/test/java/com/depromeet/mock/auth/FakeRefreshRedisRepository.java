package com.depromeet.mock.auth;

import com.depromeet.auth.port.out.persistence.RefreshRedisPersistencePort;
import java.util.HashMap;
import java.util.Map;

public class FakeRefreshRedisRepository implements RefreshRedisPersistencePort {
    private Map<String, String> data = new HashMap<>();

    @Override
    public void setData(Long memberId, String refreshToken, Long expireTime) {
        if (data.get(memberId.toString()).isEmpty()) {
            data.put(memberId.toString(), refreshToken);
        } else {
            data.replace(memberId.toString(), refreshToken);
        }
    }

    @Override
    public String getData(Long memberId) {
        return data.get(memberId.toString());
    }

    @Override
    public void deleteData(Long memberId) {
        data.remove(memberId.toString());
    }
}
