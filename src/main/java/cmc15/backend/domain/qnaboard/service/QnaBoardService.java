package cmc15.backend.domain.qnaboard.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.InsuranceType;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.entity.QnaBoard;
import cmc15.backend.domain.qnaboard.repository.QnaBoardRepository;
import cmc15.backend.domain.qnaboard.validate.QnaBoardValidator;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cmc15.backend.global.Result.NOT_FOUND_QNA_BOARD;
import static cmc15.backend.global.Result.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardValidator qnaBoardValidator;
    private final OpenAiChatModel openAiChatModel;
    private final QnaBoardRepository qnaBoardRepository;
    private final AccountRepository accountRepository;

    /**
     * @return QnaBoardResponse.Input
     * @apiNote 질문하기 API
     */
    @Transactional
    public QnaBoardResponse.Input inputQuesion(final Long accountId, final QnaBoardRequest.Input request) {
        qnaBoardValidator.validateInputQuesion(request.getQuesion());
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        String prompt = "너는 보험설계사야. 너에게 보험에 대한 질문을 할거야. 질문에 대해서 300자 이내로 설명해주고, 보험을 2개 추천해줘. 보험 추천 방식은 아래 형식을 그대로 사용해줘. 어떤 질문을 하던 보험 상품을 무조건 2개 추천해줘\n" +
                "------\n" +
                "[보험 상품 추천]\n" +
                "%추천 보험사% - %보험 상품1%\n" +
                "%보험 상품1%의 추천 이유와 장단점 등 (100자이내)\n" +
                "%추천 보험사% - %보험 상품2%\n" +
                "%보험 상품2%의 추천 이유와 장단점 등 (100자이내)";

        String call = openAiChatModel.call(prompt + request.getQuesion());

        return QnaBoardResponse.Input.to(qnaBoardRepository.save(QnaBoard.builder()
                .account(account)
                .quesion(request.getQuesion())
                .answer(call)
                .isShare(request.getIsShare())
                .insuranceType(request.getInsuranceType())
                .build())
        );
    }

    /**
     * @return QnaBoardResponse.ReadQuesionTitles
     * @apiNote 내 질문 제목 리스트 조회 API
     */
    public List<QnaBoardResponse.ReadQuesionTitle> readQuesionTitles(Long accountId) {
        List<QnaBoard> qnaBoards = qnaBoardRepository.findByAccount_AccountId(accountId);
        return qnaBoards.stream().map(QnaBoardResponse.ReadQuesionTitle::to).toList();
    }

    /**
     * @return Page<QnaBoardResponse.ReadQuestion>
     * @apiNote Q&A 게시판 글 페이지 조회 API
     */
    // TODO: 8/4/24 QueryDSL 사용 필요해보임
    public Page<QnaBoardResponse.ReadQuestion> readQuestions(Long accountId, Integer page, String insuranceType) {
        PageRequest paging = PageRequest.of(page, 10, Sort.Direction.ASC, "qnaBoardId");

        if (insuranceType != null) {
            Page<QnaBoard> qnaBoardPage = qnaBoardRepository.findByInsuranceTypeAndIsShare(InsuranceType.valueOf(insuranceType), paging, true);
            return qnaBoardPage.map(QnaBoardResponse.ReadQuestion::to);
        }

        Page<QnaBoard> qnaBoardPage = qnaBoardRepository.findAll(paging);
        return qnaBoardPage.map(QnaBoardResponse.ReadQuestion::to);
    }

    /**
     * @return QnaBoardResponse.ReadQuestion
     * @apiNote 질문 게시판 상세 조회 API
     */
    public QnaBoardResponse.ReadQuestion readQuestion(final Long accountId, final Long qnaBoardId) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(qnaBoardId).orElseThrow(() -> new CustomException(NOT_FOUND_QNA_BOARD));
        return QnaBoardResponse.ReadQuestion.to(qnaBoard);
    }

}
