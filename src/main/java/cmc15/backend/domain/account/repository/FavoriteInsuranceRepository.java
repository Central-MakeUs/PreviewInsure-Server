package cmc15.backend.domain.account.repository;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.FavoriteInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteInsuranceRepository extends JpaRepository<FavoriteInsurance, Long> {

    List<FavoriteInsurance> findByAccount(Account account);
}
