package co.nxtgrid.tokens.repository;

import co.nxtgrid.tokens.entity.Token;
import co.nxtgrid.tokens.service.impl.TimelineRequest;
import co.nxtgrid.tokens.service.impl.validate.TokenTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokensRepository extends JpaRepository<Token, Long> {

    Token findByRef(String ref);

    Token findByTokenNo(String tokenNo);

    List<Token> findByUserRefOrderByCreatedAtDesc(String userRef);

    int countByUserRef(String userRef);

    Long deleteByTokenNo(String tokenNo);

    @Query("SELECT COUNT(DISTINCT tk.tokenType) FROM Token tk WHERE userRef = :userRef")
    int countTokenTypeByUserRef(@Param("userRef") String userRef);

    @Query("SELECT COUNT(DISTINCT tk.meterNo) FROM Token tk WHERE userRef = :userRef")
    int countDistinctMeterNoByUserRef(@Param("userRef") String userRef);

    @Query(value = "SELECT * FROM (SELECT date_part('month', created_at\\:\\:date) AS month, date_part('year', created_at\\:\\:date) AS year, count(*) AS tokens FROM tokens WHERE user_ref = :userRef GROUP BY month, year ORDER BY year DESC,month DESC limit :months) AS res ORDER BY year,month ASC",
            nativeQuery = true)
    List<TimelineRequest> getTimelineRequests(@Param("userRef") String userRef,
                                              @Param("months") int months);

    @Query(value = "SELECT count(token_type) AS count, token_type AS type FROM tokens WHERE user_ref = :userRef GROUP BY token_type",
            nativeQuery = true)
    List<TokenTypeCount> getTokenTypes(@Param("userRef") String userRef);
}