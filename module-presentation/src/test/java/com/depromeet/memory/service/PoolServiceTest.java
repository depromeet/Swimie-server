package com.depromeet.memory.service;

import static org.assertj.core.api.Assertions.*;

import com.depromeet.member.Member;
import com.depromeet.memory.fixture.MemberFixture;
import com.depromeet.memory.fixture.PoolFixture;
import com.depromeet.memory.mock.FakeMemberRepository;
import com.depromeet.memory.mock.FakePoolRepository;
import com.depromeet.pool.FavoritePool;
import com.depromeet.pool.Pool;
import com.depromeet.pool.PoolSearch;
import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInfoResponse;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchInfoResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import com.depromeet.pool.service.PoolService;
import com.depromeet.pool.service.PoolServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PoolServiceTest {
    private PoolService poolService;
    private FakePoolRepository poolRepository;
    private FakeMemberRepository memberRepository;
    private Long memberId;
    private Member member;

    @BeforeEach
    void init() {
        poolRepository = new FakePoolRepository();
        memberRepository = new FakeMemberRepository();

        memberId = 1L;
        String memberRole = "USER";
        member = MemberFixture.make(memberId, memberRole);
        member = memberRepository.save(member);

        poolService = new PoolServiceImpl(poolRepository, memberRepository);

        String poolName = "테스트 수영장";
        String poolAddress = "테스트시 테스트구";
        Integer poolLane = 25;
        Pool pool = PoolFixture.make(poolName, poolAddress, poolLane);
        pool = poolRepository.save(pool);

        String secondPoolName = "두번째 테스트 수영장";
        String secondPoolAddress = "뉴테스트시 뉴테스트구";
        Integer secondPoolLane = 50;
        Pool secondPool = PoolFixture.make(secondPoolName, secondPoolAddress, secondPoolLane);
        secondPool = poolRepository.save(secondPool);

        FavoritePool favoritePool = FavoritePool.builder().pool(pool).member(member).build();
        favoritePool = poolRepository.saveFavoritePool(favoritePool);

        PoolSearch poolSearch = PoolSearch.builder().pool(pool).member(member).build();
        poolSearch = poolRepository.savePoolSearch(poolSearch);
    }

    @Test
    void 이름쿼리로_수영장을_조회합니다() throws Exception {
        // given
        String nameQuery = "테스트";

        // when
        PoolSearchResponse response = poolService.findPoolsByName(nameQuery);
        List<PoolInfoResponse> poolInfos = response.poolInfos();

        // then
        assertThat(poolInfos.size()).isEqualTo(2);
        assertThat(poolInfos.get(0).poolId()).isEqualTo(1L);
        assertThat(poolInfos.get(0).name()).isEqualTo("테스트 수영장");
        assertThat(poolInfos.get(0).address()).isEqualTo("테스트시 테스트구");
    }

    @Test
    void 즐겨찾기_수영장_및_최근검색_수영장을_조회합니다() throws Exception {
        // given
        memberId = 1L;

        // when
        PoolInitialResponse response = poolService.getFavoriteAndSearchedPools(memberId);
        List<PoolInfoResponse> favoritePools = response.favoritePools();
        List<PoolSearchInfoResponse> searchedPools = response.searchedPools();

        // then
        assertThat(favoritePools.size()).isEqualTo(1);
        assertThat(favoritePools.getFirst().name()).isEqualTo("테스트 수영장");
        assertThat(favoritePools.getFirst().address()).isEqualTo("테스트시 테스트구");

        assertThat(searchedPools.size()).isEqualTo(1);
        assertThat(searchedPools.getFirst().name()).isEqualTo("테스트 수영장");
        assertThat(searchedPools.getFirst().address()).isEqualTo("테스트시 테스트구");
    }

    @Test
    void 수영장을_즐겨찾기에_등록합니다() throws Exception {
        // given
        Long newFavoritePoolId = 2L;
        FavoritePoolCreateRequest request = new FavoritePoolCreateRequest(newFavoritePoolId);

        // when
        String location = poolService.putFavoritePool(memberId, request);

        // then
        assertThat(location).isEqualTo(String.valueOf(newFavoritePoolId));
    }

    @Test
    void 수영장을_즐겨찾기에서_삭제합니다() throws Exception {
        // given
        Long removeTargetPoolId = 1L;
        FavoritePoolCreateRequest request = new FavoritePoolCreateRequest(removeTargetPoolId);

        // when
        String location = poolService.putFavoritePool(memberId, request);

        // then
        assertThat(location).isNull();
    }

    @Test
    void 수영장_검색_로그를_생성합니다() throws Exception {
        // given
        Long loggingTargetPoolId = 2L;

        // when
        String location = poolService.createSearchLog(member, loggingTargetPoolId);

        // then
        assertThat(location).isEqualTo(String.valueOf(loggingTargetPoolId));
    }
}
