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

import java.util.ArrayList;
import java.util.List;

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
        StringBuilder answer = new StringBuilder();

        qnaBoardValidator.validateInputQuesion(request.getQuesion());
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        String prompt = "너는 보험설계사야. 너에게 보험에 대한 질문을 할거야. 질문에 답해주고, 관련되어서 친절하고 자세하게 보험을 추천해줘. 보험은 아래와 같이 추천해주되, 실제 존재하는걸 무조건 3개 추천해줘\\n------\\n[질문의 답변 300자이내]\\n\\n[실제 존재하는 보험 상품 추천]\\n%보험 상품1%\\n%보험 상품1%의 추천 이유와 장단점 등 (100자이내)\\n가입할 수 있는 %URL 이름% : %URL% (하이퍼링크 x 통신 프로토콜이 제외하고 www로 시작하는 URL로 전달)\\n\\n\\n------\\n";
        String call = openAiChatModel.call(prompt + request.getQuesion());

        List<String> urlList = parsingUrl(answer, call);
        List<QnaBoardResponse.Link> links = getLinks(urlList);
        StringBuilder recommendLinks = new StringBuilder();
        for (QnaBoardResponse.Link link : links) {
            String insuranceCompany = link.getInsuranceCompany();
            String insuranceLink = link.getInsuranceLink();
            recommendLinks.append(insuranceCompany).append("&").append(insuranceLink).append("|");
        }

        return QnaBoardResponse.Input.to(qnaBoardRepository.save(QnaBoard.builder()
                        .account(account)
                        .quesion(request.getQuesion())
                        .answer(answer.toString())
                        .isShare(request.getIsShare())
                        .insuranceType(request.getInsuranceType())
                        .recommendLinks(recommendLinks.toString())
                        .build()),
                links
        );
    }

    private static List<String> parsingUrl(StringBuilder answer, String call) {
        String[] callSplit = call.split("\n");
        List<String> urlList = new ArrayList<>();
        for (String context : callSplit) {
            if (context.contains("URL")) {
                String result = context.split(":")[1].trim();
                urlList.add(result);
            } else {
                answer.append(context).append("\n");
            }
        }
        return urlList;
    }

    private static List<QnaBoardResponse.Link> getLinks(List<String> urlList) {
        List<QnaBoardResponse.Link> links = new ArrayList<>();
        if (urlList.size() == 6) {
            for (int i = 0; i < 6; i += 2) {
                QnaBoardResponse.Link link = QnaBoardResponse.Link.to(urlList.get(i), "https://" + urlList.get(i + 1));
                links.add(link);
            }
        }
        return links;
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
    public Page<QnaBoardResponse.ReadQuestion> readQuestions(Long accountId, Integer page, InsuranceType insuranceType) {
        PageRequest paging = PageRequest.of(page, 10, Sort.Direction.ASC, "qnaBoardId");

        if (insuranceType != null) {
            Page<QnaBoard> qnaBoardPage = qnaBoardRepository.findByInsuranceTypeAndIsShare(insuranceType, paging, true);
            return qnaBoardPage.map(qnaBoard -> {
                String recommendLinks = qnaBoard.getRecommendLinks();
                List<QnaBoardResponse.Link> links = new ArrayList<>();

                if (recommendLinks != null && !recommendLinks.equals("")) {
                    String[] split = recommendLinks.split("\\|");
                    for (String s : split) {
                        String[] split1 = s.split("&");
                        String insuranceCompany = split1[0];
                        String insuranceLink = split1[1];
                        QnaBoardResponse.Link link = QnaBoardResponse.Link.to(insuranceCompany, insuranceLink);
                        links.add(link);
                    }
                }

                return QnaBoardResponse.ReadQuestion.to(qnaBoard, links);
            });
        }

        Page<QnaBoard> qnaBoardPage = qnaBoardRepository.findAll(paging);
        return qnaBoardPage.map(qnaBoard -> {
            String recommendLinks = qnaBoard.getRecommendLinks();
            List<QnaBoardResponse.Link> links = new ArrayList<>();

            if (recommendLinks != null && !recommendLinks.equals("")) {
                String[] split = recommendLinks.split("\\|");
                for (String s : split) {
                    String[] split1 = s.split("&");
                    String insuranceCompany = split1[0];
                    String insuranceLink = split1[1];
                    QnaBoardResponse.Link link = QnaBoardResponse.Link.to(insuranceCompany, insuranceLink);
                    links.add(link);
                }
            }

            return QnaBoardResponse.ReadQuestion.to(qnaBoard, links);
        });
    }
}
