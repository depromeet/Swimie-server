package com.depromeet.service.pool;

import static org.assertj.core.api.Assertions.*;

import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.fixture.domain.pool.PoolFixture;
import com.depromeet.member.domain.Member;
import com.depromeet.mock.member.FakeMemberRepository;
import com.depromeet.mock.pool.FakePoolRepository;
import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import com.depromeet.pool.domain.vo.PoolSearchPage;
import com.depromeet.pool.port.in.command.FavoritePoolCommand;
import com.depromeet.pool.service.PoolService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PoolServiceTest {
    private PoolService poolService;
    private FakePoolRepository poolRepository;
    private FakeMemberRepository memberRepository;
    private Long memberId;
    private Member member;
    private Pool pool;
    private FavoritePool favoritePool;
    private PoolSearch poolSearch;

    @BeforeEach
    void init() {
        poolRepository = new FakePoolRepository();
        memberRepository = new FakeMemberRepository();

        memberId = 1L;
        String memberRole = "USER";
        member = MemberFixture.make();
        member = memberRepository.save(member);

        // poolService = new PoolService(poolRepository, memberRepository);
        poolService = new PoolService(poolRepository, memberRepository);

        String poolName = "테스트 수영장";
        String poolAddress = "테스트시 테스트구";
        Integer poolLane = 25;
        pool = PoolFixture.make(poolName, poolAddress, poolLane);
        pool = poolRepository.save(pool);

        String secondPoolName = "두번째 테스트 수영장";
        String secondPoolAddress = "뉴테스트시 뉴테스트구";
        Integer secondPoolLane = 50;
        Pool secondPool = PoolFixture.make(secondPoolName, secondPoolAddress, secondPoolLane);
        secondPool = poolRepository.save(secondPool);

        favoritePool = FavoritePool.builder().pool(pool).member(member).build();
        favoritePool = poolRepository.saveFavoritePool(favoritePool);

        poolSearch = PoolSearch.builder().pool(pool).member(member).build();
        poolSearch = poolRepository.savePoolSearch(poolSearch);
    }

    @Test
    void 이름쿼리로_수영장을_조회합니다() throws Exception {
        // given
        String nameQuery = "테스트";
        Set<Long> favoritePoolIds = new HashSet<>();
        favoritePoolIds.add(favoritePool.getPool().getId());

        // when
        PoolSearchPage page = poolService.getPoolsByNameAndNotIn(nameQuery, favoritePoolIds, null);

        // then
        assertThat(page.isHasNext()).isEqualTo(false);
        assertThat(page.getPools().getFirst().getId()).isEqualTo(2L);
        assertThat(page.getPools().getFirst().getName()).isEqualTo("두번째 테스트 수영장");
        assertThat(page.getPools().getFirst().getAddress()).isEqualTo("뉴테스트시 뉴테스트구");
    }

    @Test
    void 즐겨찾기_수영장_및_최근검색_수영장을_조회합니다() throws Exception {
        // given
        memberId = 1L;

        // when
        List<FavoritePool> favoritePools = poolService.getFavoritePools(memberId);
        List<PoolSearch> searchedPools = poolService.getSearchedPools(memberId);

        // then
        assertThat(favoritePools.size()).isEqualTo(1);
        assertThat(favoritePools.getFirst().getPool().getName()).isEqualTo("테스트 수영장");
        assertThat(favoritePools.getFirst().getPool().getAddress()).isEqualTo("테스트시 테스트구");

        assertThat(searchedPools.size()).isEqualTo(1);
        assertThat(searchedPools.getFirst().getPool().getName()).isEqualTo("테스트 수영장");
        assertThat(searchedPools.getFirst().getPool().getAddress()).isEqualTo("테스트시 테스트구");
    }

    @Test
    void 수영장을_즐겨찾기에_등록합니다() throws Exception {
        // given
        Long newFavoritePoolId = 2L;
        FavoritePoolCommand command = new FavoritePoolCommand(newFavoritePoolId);

        // when
        String location = poolService.putFavoritePool(memberId, command);

        // then
        assertThat(location).isEqualTo(String.valueOf(newFavoritePoolId));
    }

    @Test
    void 수영장을_즐겨찾기에서_삭제합니다() throws Exception {
        // given
        Long removeTargetPoolId = 1L;
        FavoritePoolCommand command = new FavoritePoolCommand(removeTargetPoolId);

        // when
        String location = poolService.putFavoritePool(memberId, command);

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
