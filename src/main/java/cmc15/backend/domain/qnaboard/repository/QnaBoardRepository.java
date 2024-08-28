package cmc15.backend.domain.qnaboard.repository;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.InsuranceType;
import cmc15.backend.domain.qnaboard.entity.QnaBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {
    List<QnaBoard> findByAccount_AccountId(Long accountId);
    Page<QnaBoard> findByInsuranceTypeAndIsShare(InsuranceType insuranceType, Pageable pageable, boolean isShare);
    Page<QnaBoard> findAllByIsShare(Pageable pageable, boolean isShare);
    Optional<QnaBoard> findByQnaBoardIdAndAccount(Long qnaBoardId, Account account);
}
