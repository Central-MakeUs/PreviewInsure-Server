package cmc15.backend.domain.qnaboard.repository;

import cmc15.backend.domain.qnaboard.entity.QnaBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {
}
