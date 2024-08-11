package cmc15.backend.domain.qnaboard.validate;


import cmc15.backend.global.Constants;
import cmc15.backend.global.exception.CustomException;
import org.springframework.stereotype.Component;

import static cmc15.backend.global.Result.*;

@Component
public class QnaBoardValidator {

    /**
     * @title 메시지 등록 검증
     * @param message
     */
    public void validateInputQuesion(String message) {
        validateNotEmptyMessage(message);
        validateMessageSize((message));
        validateWord(message);
    }

    private void validateNotEmptyMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new CustomException(NOT_EMPTY_MESSAGE);
        }
    }

    private void validateMessageSize(String message) {
        if (message.length() < 4 || message.length() > 1000) {
            throw new CustomException(MESSAGE_SIZE_ERROR);
        }
    }

    private void validateWord(String message) {
        for (String bannedWord : Constants.BANNED_WORDS) {
            if (message.contains(bannedWord)) {
                throw new CustomException(USE_BANNED_WORD);
            }
        }
    }

}
