package cmc15.backend.domain.qnaboard.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.entity.QnaBoard;
import cmc15.backend.domain.qnaboard.repository.QnaBoardRepository;
import cmc15.backend.domain.qnaboard.validate.QnaBoardValidator;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cmc15.backend.global.Result.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardValidator qnaBoardValidator;
    private final OpenAiChatModel openAiChatModel;
    private final QnaBoardRepository qnaBoardRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public QnaBoardResponse.Input inputQuesion(final Long accountId, final QnaBoardRequest.Input request) {
        qnaBoardValidator.validateInputQuesion(request.getQuesion());
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        String call = openAiChatModel.call(request.getQuesion());

        return QnaBoardResponse.Input.to(qnaBoardRepository.save(QnaBoard.builder()
                .account(account)
                .quesion(request.getQuesion())
                .answer(call)
                .isShare(request.getIsShare())
                .insuranceType(request.getInsuranceType())
                .build()));
    }
}
