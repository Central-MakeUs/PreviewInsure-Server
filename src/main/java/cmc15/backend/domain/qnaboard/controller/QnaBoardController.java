package cmc15.backend.domain.qnaboard.controller;

import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.service.QnaBoardService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;

    /**
     * @apiNote 질문하기 API
     * @param accountId
     * @param request
     * @return QnaBoardResponse.Input
     */
    @PostMapping("/quesion")
    public CustomResponseEntity<QnaBoardResponse.Input> inputQuesion(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final QnaBoardRequest.Input request
    ) {
        return CustomResponseEntity.success(qnaBoardService.inputQuesion(accountId, request));
    }

    /**
     * @apiNote 내 질문 타이틀 조회 API
     * @param accountId
     * @return QnaBoardResponse.ReadQuesionTitles
     */
    @GetMapping("/quesion")
    public CustomResponseEntity<List<QnaBoardResponse.ReadQuesionTitle>> readQuesionTitles(
            @AuthenticationPrincipal final Long accountId
    ) {
        return CustomResponseEntity.success(qnaBoardService.readQuesionTitles(accountId));
    }
}
