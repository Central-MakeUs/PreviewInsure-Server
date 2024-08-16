package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.qnaboard.controller.QnaBoardController;
import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.service.QnaBoardService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import java.util.Arrays;
import java.util.List;

import static cmc15.backend.domain.account.entity.InsuranceType.ED;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QnaBoardControllerDocsTest extends RestDocsSupport {
    private final QnaBoardService qnaBoardService = mock(QnaBoardService.class);

    @Override
    protected Object initController() {
        return new QnaBoardController(qnaBoardService);
    }

    @DisplayName("질문 등록 API")
    @Test
    void 질문_등록_API() throws Exception {
        // given
        QnaBoardRequest.Input request = new QnaBoardRequest.Input("질문 입니다.", true, ED);

        given(qnaBoardService.inputQuesion(any(), any(QnaBoardRequest.Input.class)))
                .willReturn(QnaBoardResponse.Input.builder()
                        .qnaBoardId(1L)
                        .quesion("보험을 알아보기 쉬운 애플리케이션은 뭘까?")
                        .answer("보험을 알아보기 쉬운 애플리케이션을 찾고 계시는 군요! ...")
                        .isShare(true)
                        .insuranceType("하나손해보험")
                        .build()
                );

        ResourceSnippetParameters resources = ResourceSnippetParameters.builder()
                .tag("질문 게시판/AI")
                .summary("질문 등록(AI) API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .requestFields(
                        fieldWithPath("quesion").type(STRING).description("명령 메시지"),
                        fieldWithPath("isShare").type(BOOLEAN).description("전체 게시판 질문 내용 공유 여부 (true:1, false:0"),
                        fieldWithPath("insuranceType").type(STRING).description("보험 유형"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.qnaBoardId").type(NUMBER).description("질문 Id"),
                        fieldWithPath("data.quesion").type(STRING).description("질문"),
                        fieldWithPath("data.answer").type(STRING).description("AI 대답"),
                        fieldWithPath("data.isShare").type(BOOLEAN).description("전체 게시판 질문 공유 여부"),
                        fieldWithPath("data.insuranceType").type(STRING).description("보험 유형"))
                .build();

        RestDocumentationResultHandler document = documentHandler("add-quesion", prettyPrint(), resources);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/quesion")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("내 질문 타이틀 조회 API")
    @Test
    void 내_질문_타이틀_조회_API() throws Exception {
        // given
        given(qnaBoardService.readQuesionTitles(any()))
                .willReturn(
                        List.of(
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(1L).title("안녕하세요!").insuranceType("운전자 보험").build(),
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(2L).title("질문입니다").insuranceType("상해 보험").build(),
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(3L).title("배가고파요").insuranceType("교육 보험").build(),
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(4L).title("메뉴추천좀해줘").insuranceType("전체").build(),
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(5L).title("놀거리추천좀해줘").insuranceType("저축 보험").build())
                );

        ResourceSnippetParameters resources = ResourceSnippetParameters.builder()
                .tag("질문 게시판/AI")
                .summary("내 질문 타이틀 조회 API")
                .description("내 질문들 Id 및 질문 내용 조회")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data[].qnaBoardId").type(NUMBER).description("질문 Id"),
                        fieldWithPath("data[].title").type(STRING).description("질문"),
                        fieldWithPath("data[].insuranceType").type(STRING).description("보험 유형"))
                .build();

        RestDocumentationResultHandler document = documentHandler("read-quesion-titles", prettyPrint(), resources);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/quesion")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("QnA 게시판 페이지 조회 API")
    @Test
    void QnA_게시판_페이지_조회_API() throws Exception {
        // given
        // 데이터 생성
        List<QnaBoardResponse.ReadQuestion> content = Arrays.asList(
                new QnaBoardResponse.ReadQuestion(139L, "나는 28살인데 아직 보험에 대해 잘몰라.. 이제 들면 좋을 보험좀 추천해줄랭",
                        "당연하지! 28살이라면 건강과 재정적 안정성을 고려한 보험이 중요해. 지금 시점에서 추천할 만한 보험은 다음과 같아:\n\n1. **삼성화재 건강보험 뉴올라이프**  \n   이 보험은 종합적인 건강 보장을 제공하며, 특히 암, 뇌질환, 심장질환 등 중대 질병에 대한 보장이 강력해.  \n   장점: 다양한 특약 옵션으로 맞춤형 설계 가능.  \n   단점: 특약 추가 시 보험료 상승.  \n\n2. **메리츠화재 행복한종합보험**  \n   종합적인 의료비 보장을 제공하며, 일상생활에서 발생할 수 있는 다양한 사고와 질병을 커버해 줘.  \n   장점: 입원비와 수술비 보장이 잘 되어 있음.  \n   단점: 보험료가 다소 높은 편.  \n\n3. **KB손해보험 다이렉트 자동차보험**  \n   자동차를 소유하고 있다면 필수적인 보험으로, 사고 시 손해를 최소화해 줘.  \n   장점: 온라인 가입 시 할인 혜택 제공.  \n   단점: 일부 특약이 제한적.  \n\n이렇게 다양한 보험 상품을 통해 건강과 재정적 안정을 동시에 챙길 수 있어. 필요에 맞는 보험을 선택해서 가입하는 게 중요해."
                        ),
                new QnaBoardResponse.ReadQuestion(140L, "나는 28살인데 아직 보험에 대해 잘몰라.. 이제 들면 좋을 보험좀 추천해줄랭",
                        "28살이시면 지금부터 보험을 준비하는 것이 매우 현명한 선택입니다. 기본적으로 생명보험, 건강보험, 그리고 자동차보험을 고려하시는 것이 좋습니다. 각각의 보험 상품을 아래에 추천해드리니 참고해보세요.\n\n1. 삼성생명 종신보험\n삼성생명 종신보험은 평생 동안 보험 혜택을 제공하며, 사망 시 가족에게 재정적 안정을 제공합니다.\n장점: 평생 보장, 유가족 재정 지원\n단점: 높은 보험료\n\n2. KB손해보험 건강보험\nKB손해보험 건강보험은 다양한 질병과 상해에 대해 보장을 제공합니다.\n장점: 다양한 질병 보장, 합리적인 보험료\n단점: 특정 질병에 대한 제한\n\n3. 현대해상 자동차보험\n현대해상 자동차보험은 자동차 사고 시 다양한 보장을 제공합니다.\n장점: 다양한 사고 보장, 신속한 사고 처리\n단점: 높은 보험료\n\n이러한 보험 상품들을 통해 자신의 필요에 맞는 보험을 선택하시길 바랍니다. 추가적인 상담이 필요하면 각 보험사의 홈페이지를 방문해보세요!"
                        ),
                new QnaBoardResponse.ReadQuestion(141L, "나는 28살인데 아직 보험에 대해 잘몰라.. 이제 들면 좋을 보험좀 추천해줄랭",
                        "보험에 대해 이제 시작하려고 하시는군요! 28살이라면 지금부터 보험을 준비하면 좋은 시기입니다. 주로 고려해야 할 보험은 건강보험, 생명보험 및 실손의료보험입니다. 이 세 가지는 가장 기본적이면서도 중요한 보험 상품들입니다.\n\n[실제 존재하는 보험 상품 추천]\n\n1. 삼성화재 건강보험\n   삼성화재의 건강보험은 다양한 질병과 사고에 대한 보장을 제공하며, 갱신형과 비갱신형으로 선택할 수 있어 유연합니다. \n\n2. 한화생명 e생명보험\n   비교적 저렴한 보험료로 다양한 보장을 제공하며, 사망보험금 외에도 다양한 특약을 추가할 수 있습니다.\n\n3. 메리츠화재 실손의료보험\n   실제 지출한 의료비를 보장받을 수 있어 병원비 부담을 크게 줄여줍니다. 보험료가 저렴하고 필요한 치료 시 보장을 받을 수 있습니다.\n\n이 세 가지 보험을 고려해보세요. 각 보험사 사이트에서 더 자세한 정보를 확인하고, 필요에 따라 전문가와 상담해보는 것도 추천드립니다."
                        ),
                new QnaBoardResponse.ReadQuestion(142L, "나는 28살인데 아직 보험에 대해 잘몰라.. 이제 들면 좋을 보험좀 추천해줄랭",
                        "28살에 보험을 고민하는 것은 아주 좋은 선택입니다. 기본적으로 고려해야 할 보험은 건강보험, 생명보험, 그리고 자동차 보험입니다. 각각의 보험은 당신의 현재 상황과 미래 계획에 맞추어 선택하는 것이 중요합니다.\n\n**건강 보험**\n1. **삼성화재 건강보험**\n   - 추천 이유: 종합적인 보장이 가능하며, 갱신형과 비갱신형 선택 가능.\n   - 장단점: 넓은 보장 범위, 다소 높은 보험료.\n\n**생명 보험**\n2. **교보생명 생명보험**\n   - 추천 이유: 다양한 플랜 제공, 우수한 고객 서비스.\n   - 장단점: 높은 보장 금액, 가입 절차가 다소 복잡.\n\n**자동차 보험**\n3. **현대해상 자동차보험**\n   - 추천 이유: 다양한 할인 혜택, 신속한 사고 처리.\n   - 장단점: 많은 혜택, 보험료가 다소 높을 수 있음.\n\n이 보험들을 통해 건강과 자산을 보호할 수 있습니다. 각 보험사의 웹사이트에서 더 자세한 정보를 확인하고, 자신에게 맞는 보험을 선택해보세요."
                        )
        );

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, 10);

        // PageImpl 객체 생성
        PageImpl<QnaBoardResponse.ReadQuestion> page = new PageImpl<>(content, pageable, 4);


        given(qnaBoardService.readQuestions(any(), any()))
                .willReturn(page);

        ResourceSnippetParameters resources = ResourceSnippetParameters.builder()
                .tag("질문 게시판/AI")
                .summary("질문 페이지 조회 API")
                .description("질문 게시판 페이지 조회 API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .queryParameters(
                        parameterWithName("insuranceType").description("조회할 타입(전체보기는 null)").optional(),
                        parameterWithName("page").description("조회할 페이지 번호")
                )
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메시지"),
                        fieldWithPath("data").type(OBJECT).description("데이터 객체"),
                        fieldWithPath("data.totalPages").type(NUMBER).description("총 페이지 수"),
                        fieldWithPath("data.totalElements").type(NUMBER).description("총 요소 수"),
                        fieldWithPath("data.size").type(NUMBER).description("페이지 크기"),
                        fieldWithPath("data.content").type(ARRAY).description("질문과 답변 목록"),
                        fieldWithPath("data.content[].qnaBoardId").type(NUMBER).description("QnA 게시판 ID"),
                        fieldWithPath("data.content[].question").type(STRING).description("질문"),
                        fieldWithPath("data.content[].answer").type(STRING).description("답변"),
                        fieldWithPath("data.number").type(NUMBER).description("현재 페이지 번호"),
                        fieldWithPath("data.sort.empty").type(BOOLEAN).description("정렬이 비어있는지 여부"),
                        fieldWithPath("data.sort.unsorted").type(BOOLEAN).description("정렬이 정렬되지 않은 상태인지 여부"),
                        fieldWithPath("data.sort.sorted").type(BOOLEAN).description("정렬 상태"),
                        fieldWithPath("data.pageable.pageNumber").type(NUMBER).description("현재 페이지 번호"),
                        fieldWithPath("data.pageable.pageSize").type(NUMBER).description("페이지 크기"),
                        fieldWithPath("data.pageable.sort.empty").type(BOOLEAN).description("정렬이 비어있는지 여부"),
                        fieldWithPath("data.pageable.sort.unsorted").type(BOOLEAN).description("정렬이 정렬되지 않은 상태인지 여부"),
                        fieldWithPath("data.pageable.sort.sorted").type(BOOLEAN).description("정렬 상태"),
                        fieldWithPath("data.pageable.offset").type(NUMBER).description("오프셋"),
                        fieldWithPath("data.pageable.paged").type(BOOLEAN).description("페이지 매김 여부"),
                        fieldWithPath("data.pageable.unpaged").type(BOOLEAN).description("비페이지 매김 여부"),
                        fieldWithPath("data.numberOfElements").type(NUMBER).description("요소 수"),
                        fieldWithPath("data.first").type(BOOLEAN).description("첫 페이지 여부"),
                        fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("data.empty").type(BOOLEAN).description("페이지가 비어있는지 여부"))
                .build();

        RestDocumentationResultHandler document = documentHandler("read-quesions", prettyPrint(), prettyPrint(), resources);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/questions")
                        .header("Authorization", "Bearer AccessToken")
                        .param("page", "0")
                        .param("insuranceType", "LF"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("QnA 게시판 상세 조회 API")
    @Test
    void 질문_게시판_상세_조회_API() throws Exception {
        // given
        given(qnaBoardService.readQuestion(any(), any()))
                .willReturn(new QnaBoardResponse.ReadQuestion(148L, "나는 28살인데 아직 보험에 대해 잘몰라.. 이제 들면 좋을 보험좀 추천해줄랭", "28살이시라면, 지금이 보험 가입을 고려하기에 아주 좋은 시기입니다. 아직 젊고 건강할 때 가입하면 보험료도 저렴하고, 보장 범위도 넓을 수 있습니다. 먼저 고려해볼 보험 상품을 몇 가지 추천드리겠습니다.\n" +
                        "\n" +
                        "1. 건강보험\n" +
                        "   - 삼성화재의 '건강보험'\n" +
                        "   - 추천 이유: 젊을 때 가입하면 보험료가 저렴하고, 다양한 질병과 사고를 포괄적으로 보장해줍니다.\n" +
                        "\n" +
                        "2. 생명보험\n" +
                        "   - 한화생명의 '생명보험'\n" +
                        "   - 추천 이유: 사망 시 가족에게 경제적 지원을 제공하며, 일부 상품은 건강 보장도 함께 제공합니다.\n" +
                        "\n" +
                        "3. 저축보험\n" +
                        "   - 교보생명의 '저축보험'\n" +
                        "   - 추천 이유: 저축과 보험을 동시에 할 수 있어 미래 자산을 마련하는데 유용합니다.\n" +
                        "\n" +
                        "이렇게 세 가지 보험을 추천드립니다. 각 보험은 보장 내용과 혜택이 다르므로, 자신의 필요와 상황에 맞춰 선택하시면 좋겠습니다. 추가로 궁금한 점이 있으면 언제든지 물어보세요!\n"
                        ));

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("질문 게시판/AI")
                .summary("질문 페이지 조회 API")
                .description("질문 게시판 페이지 조회 API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .queryParameters(
                        parameterWithName("qnaBoardId").description("조회할 게시글 Id"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메시지"),
                        fieldWithPath("data.qnaBoardId").type(NUMBER).description("QnA 게시판 ID"),
                        fieldWithPath("data.question").type(STRING).description("질문"),
                        fieldWithPath("data.answer").type(STRING).description("답변"))
                .build();


        RestDocumentationResultHandler document = documentHandler("read-question", prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/question/detail")
                        .header("Authorization", "Bearer AccessToken")
                        .param("qnaBoardId", "148"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}
