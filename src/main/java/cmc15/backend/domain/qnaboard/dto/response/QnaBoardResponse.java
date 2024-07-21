package cmc15.backend.domain.qnaboard.dto.response;

import cmc15.backend.domain.qnaboard.entity.QnaBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QnaBoardResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Input {
        private Long qnaBoardId;
        private String quesion;
        private String answer;
        private Boolean isShare;

        public static QnaBoardResponse.Input to(QnaBoard qnaBoard) {
            return Input.builder()
                    .qnaBoardId(qnaBoard.getQnaBoardId())
                    .quesion(qnaBoard.getQuesion())
                    .answer(qnaBoard.getAnswer())
                    .isShare(qnaBoard.getIsShare())
                    .build();
        }
    }
}
