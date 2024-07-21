package cmc15.backend.domain.qnaboard.controller;

import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.service.QnaBoardService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;

    @GetMapping("/quesion")
    public CustomResponseEntity<QnaBoardResponse.Input> inputQuesion(
            @RequestParam @Valid final String message,
            @AuthenticationPrincipal final Long accountId
    ) {
        return CustomResponseEntity.success(qnaBoardService.inputQuesion(message, accountId));
    }
}
