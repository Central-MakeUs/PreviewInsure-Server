package cmc15.backend.domain.qnaboard.validate;


import cmc15.backend.global.exception.CustomException;
import org.springframework.stereotype.Component;

import static cmc15.backend.global.Result.MESSAGE_SIZE_ERROR;
import static cmc15.backend.global.Result.NOT_EMPTY_MESSAGE;

@Component
public class QnaBoardValidator {

    /**
     * @title 메시지 등록 검증
     * @param message
     */
    public void validateInputQuesion(String message) {
        validateNotEmptyMessage(message);
        validateMessageSize((message));
    }

    private void validateNotEmptyMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new CustomException(NOT_EMPTY_MESSAGE);
        }
    }

    private void validateMessageSize(String message) {
        if (message.length() < 4 || message.length() > 300) {
            throw new CustomException(MESSAGE_SIZE_ERROR);
        }
    }

}
