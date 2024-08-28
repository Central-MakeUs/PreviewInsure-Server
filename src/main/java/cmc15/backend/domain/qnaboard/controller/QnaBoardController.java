package cmc15.backend.domain.qnaboard.controller;

import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.service.QnaBoardService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;

    /**
     * @param accountId
     * @param request
     * @return QnaBoardResponse.Input
     * @apiNote 질문하기 API
     */
    @PostMapping("/quesion")
    public CustomResponseEntity<QnaBoardResponse.Input> inputQuesion(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final QnaBoardRequest.Input request
    ) {
        return CustomResponseEntity.success(qnaBoardService.inputQuesion(accountId, request));
    }

    /**
     * @param accountId
     * @return QnaBoardResponse.ReadQuesionTitles
     * @apiNote 내 질문 타이틀 조회 API
     */
    @GetMapping("/quesion")
    public CustomResponseEntity<List<QnaBoardResponse.ReadQuesionTitle>> readQuesionTitles(
            @AuthenticationPrincipal final Long accountId
    ) {
        return CustomResponseEntity.success(qnaBoardService.readQuesionTitles(accountId));
    }

    /**
     * @param accountId
     * @param page
     * @param insuranceType
     * @return
     * @apiNote Qna 게시판 페이지 조회 API
     */
    @GetMapping("/questions")
    public CustomResponseEntity<Page<QnaBoardResponse.ReadQuestion>> readQuestions(
            @RequestParam final Integer page,
            @RequestParam(required = false) final String insuranceType
    ) {
        return CustomResponseEntity.success(qnaBoardService.readQuestions(page, insuranceType));
    }

    /**
     * @param accountId
     * @param qnaBoardId
     * @return
     * @apiNote 질문 게시판 상세 조회 API
     */
    @GetMapping("/question/detail")
    public CustomResponseEntity<QnaBoardResponse.ReadQuestion> readQuestion(
            @AuthenticationPrincipal final Long accountId,
            @RequestParam final Long qnaBoardId
    ) {
        return CustomResponseEntity.success(qnaBoardService.readQuestion(accountId, qnaBoardId));
    }

    /**
     * @param accountId
     * @param request
     * @apiNote 질문 업데이트 API
     */
    @PatchMapping("/question")
    public CustomResponseEntity<QnaBoardResponse.Input> updateQuesion(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final QnaBoardRequest.Update request
    ) {
        return CustomResponseEntity.success(qnaBoardService.updateQuesion(accountId, request));
    }
}
