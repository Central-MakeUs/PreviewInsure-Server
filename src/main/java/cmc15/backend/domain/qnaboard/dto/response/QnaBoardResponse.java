package cmc15.backend.domain.qnaboard.dto.response;

import cmc15.backend.domain.qnaboard.entity.QnaBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

public class QnaBoardResponse {

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class Input {
        private Long qnaBoardId;
        private String quesion;
        private String answer;
        private Boolean isShare;
        private String insuranceType;

        public static QnaBoardResponse.Input to(final QnaBoard qnaBoard) {
            return Input.builder()
                    .qnaBoardId(qnaBoard.getQnaBoardId())
                    .quesion(qnaBoard.getQuesion())
                    .answer(qnaBoard.getAnswer())
                    .isShare(qnaBoard.getIsShare())
                    .insuranceType(qnaBoard.getInsuranceType().getTypeContent())
                    .build();
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class ReadQuesionTitle {
        private Long qnaBoardId;
        private String title;
        private String insuranceType;

        public static ReadQuesionTitle to(QnaBoard qnaBoard) {
            return ReadQuesionTitle.builder()
                    .qnaBoardId(qnaBoard.getQnaBoardId())
                    .title(qnaBoard.getQuesion())
                    .insuranceType(qnaBoard.getInsuranceType() != null ? qnaBoard.getInsuranceType().getTypeContent() : "전체")
                    .build();
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class ReadQuestion {
        private Long qnaBoardId;
        private String question;
        private String answer;
        private List<String> links;

        public static ReadQuestion to(QnaBoard qnaBoard) {
            return ReadQuestion.builder()
                    .qnaBoardId(qnaBoard.getQnaBoardId())
                    .question(qnaBoard.getQuesion())
                    .answer(qnaBoard.getAnswer())
                    .links(Arrays.stream(qnaBoard.getRecommendLinks().split("&")).toList())
                    .build();
        }
    }
}
