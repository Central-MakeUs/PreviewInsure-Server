package cmc15.backend.domain.qnaboard.controller;

import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.service.QnaBoardService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;

    @PostMapping("/quesion")
    public CustomResponseEntity<QnaBoardResponse.Input> inputQuesion(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final QnaBoardRequest.Input request
    ) {
        return CustomResponseEntity.success(qnaBoardService.inputQuesion(accountId, request));
    }
}
