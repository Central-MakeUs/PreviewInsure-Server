package cmc15.backend.domain.account.repository;

import cmc15.backend.domain.account.entity.Account;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    Optional<Account> findByAppleAccount(String sub);

    @Query("SELECT a.deleteAt FROM Account a WHERE a.email = :email")
    LocalDateTime findByDeleteAccount(@Param("email") String email);

    @Query("SELECT a.deleteAt FROM Account a WHERE a.appleAccount = :sub")
    LocalDateTime findByDeleteAppleAccount(@Param("sub") String sub);
}
