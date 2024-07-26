package cmc15.backend.domain.account.repository;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.entity.InsuranceCompany;
import cmc15.backend.domain.account.entity.InsuranceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInsuranceRepository extends JpaRepository<AccountInsurance, Long> {

    boolean existsByAccountAndInsuranceTypeAndInsuranceCompany(Account account, InsuranceType insuranceType, InsuranceCompany insuranceCompany);
}
