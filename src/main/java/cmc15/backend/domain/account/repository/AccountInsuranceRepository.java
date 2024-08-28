package cmc15.backend.domain.account.repository;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.entity.InsuranceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountInsuranceRepository extends JpaRepository<AccountInsurance, Long> {

    boolean existsByAccountAndInsuranceTypeAndInsuranceCompany(Account account, InsuranceType insuranceType, String insuranceCompany);

    Optional<AccountInsurance> findByAccountAndInsuranceType(Account account, InsuranceType insuranceType);

    List<AccountInsurance> findByAccount(Account account);

    void deleteByAccount(Account account);

    Optional<AccountInsurance> findByAccountInsuranceIdAndInsuranceTypeAndAccount(Long accountInsuranceId, InsuranceType insuranceType, Account account);
}
