package com.intuit.craftdemo.repository;

import com.intuit.craftdemo.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {


    @Query("""
            select t from tokens t inner join Driver d on t.driver.id = d.id
            where d.id = :driverId and (t.expired =false or t.revoked=false)
        """
    )
    List<Token> findAllValidTokenByDriver(Long driverId);


    Optional<Token> findByToken(String token);

    @Modifying
    @Query("UPDATE tokens t SET t.expired = :expired, t.revoked = :revoked WHERE t.token = :tokenValue")
    void updateTokenStatusByValue(
            @Param("tokenValue") String tokenValue,
            @Param("expired") boolean expired,
            @Param("revoked") boolean revoked
    );
}
