package cmc15.backend.domain.qnaboard.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.InsuranceType;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import cmc15.backend.domain.qnaboard.dto.response.InsuranceCallResponse;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.entity.QnaBoard;
import cmc15.backend.domain.qnaboard.repository.QnaBoardRepository;
import cmc15.backend.domain.qnaboard.validate.QnaBoardValidator;
import cmc15.backend.global.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static cmc15.backend.global.Result.*;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardValidator qnaBoardValidator;
    private final OpenAiChatModel openAiChatModel;
    private final ObjectMapper objectMapper;
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

        String prompt = "너는 보험설계사야. 너에게 보험에 대한 질문을 할거야. 질문에 대해서 300자 이내로 설명해주고, 한국에서 사용할 수 있는 실제 보험 상품을 무조건 3개 추천해줘. 보험 추천 방식은 \n" +
                "ObjectMapper로 파싱할 수 있는 JSON 형태로 답변을 해줄거야, 아래는 예시야.\n" +
                "{\n" +
                "\"context\" : \"질문에 대한 답변 300자 이내\",\n" +
                "\"links\" : [\n" +
                "{\"insuranceCompany\" : \"추천된 보험회사 이름\", \"link\" : \"추천된 보험회사 사이트 주소\"}, ...]\n" +
                "}\n\n" +
                "질문 : ";

        String call = openAiChatModel.call(prompt + request.getQuesion());
        InsuranceCallResponse insuranceCallResponse = callParsingJson(call);
        List<InsuranceCallResponse.InsuranceLink> links = insuranceCallResponse.getLinks();
        return QnaBoardResponse.Input.to(qnaBoardRepository.save(QnaBoard.builder()
                .account(account)
                .quesion(request.getQuesion())
                .answer(insuranceCallResponse.getContext())
                .isShare(request.getIsShare())
                .insuranceType(request.getInsuranceType())
                .insuranceLinks(getInsuranceLinks(links))
                .build()), links
        );
    }

    private static String getInsuranceLinks(List<InsuranceCallResponse.InsuranceLink> links) {
        StringBuilder sb = new StringBuilder();
        for (InsuranceCallResponse.InsuranceLink link : links) {
            String insuranceCompany = link.getInsuranceCompany();
            String insuranceLink = link.getLink();
            sb.append(insuranceCompany).append(",").append(insuranceLink).append("$");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private InsuranceCallResponse callParsingJson(String call) {
        try {
            return objectMapper.readValue(call, InsuranceCallResponse.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(ERROR_PROMPT);
        }
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
    public Page<QnaBoardResponse.ReadQuestion> readQuestions(Integer page, String insuranceType) {
        PageRequest paging = PageRequest.of(page, 10, Sort.Direction.DESC, "qnaBoardId");

        if (insuranceType != null) {
            Page<QnaBoard> qnaBoardPage = qnaBoardRepository.findByInsuranceTypeAndIsShare(InsuranceType.valueOf(insuranceType), paging, true);
            return getReadQuestions(qnaBoardPage);
        }

        Page<QnaBoard> qnaBoardPage = qnaBoardRepository.findAllByIsShare(paging, true);
        return getReadQuestions(qnaBoardPage);
    }

    private Page<QnaBoardResponse.ReadQuestion> getReadQuestions(Page<QnaBoard> qnaBoardPage) {
        return qnaBoardPage.map(qnaBoard -> {
            List<InsuranceCallResponse.InsuranceLink> links = new ArrayList<>();
            String insuranceLinks = qnaBoard.getInsuranceLinks();
            if (insuranceLinks != null) {
                for (String object : insuranceLinks.split("\\$")) {
                    System.out.println(object);
                    String[] recommendInsurance = object.split(",");
                    String insuranceCompany = recommendInsurance[0];
                    String link = recommendInsurance[1];
                    links.add(InsuranceCallResponse.InsuranceLink.to(insuranceCompany, link));
                }
            }
            return QnaBoardResponse.ReadQuestion.to(qnaBoard, links);
        });
    }

    /**
     * @return QnaBoardResponse.ReadQuestion
     * @apiNote 질문 게시판 상세 조회 API
     */
    public QnaBoardResponse.ReadQuestion readQuestion(final Long accountId, final Long qnaBoardId) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(qnaBoardId).orElseThrow(() -> new CustomException(NOT_FOUND_QNA_BOARD));
        List<InsuranceCallResponse.InsuranceLink> links = new ArrayList<>();

        String insuranceLinks = qnaBoard.getInsuranceLinks();
        if (insuranceLinks != null) {
            for (String object : insuranceLinks.split("\\$")) {
                String[] recommendInsurance = object.split(",");
                String insuranceCompany = recommendInsurance[0];
                String link = recommendInsurance[1];
                links.add(InsuranceCallResponse.InsuranceLink.to(insuranceCompany, link));
            }
        }

        return QnaBoardResponse.ReadQuestion.to(qnaBoard, links);
    }
}
