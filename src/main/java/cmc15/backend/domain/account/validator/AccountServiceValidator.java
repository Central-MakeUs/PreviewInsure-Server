package cmc15.backend.domain.account.validator;

import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.global.exception.CustomException;
import org.springframework.stereotype.Component;

import static cmc15.backend.global.Result.ALREADY_ADD_INSURANCE;
import static cmc15.backend.global.Result.NOT_EMPTY_GENDER;

@Component
public class AccountServiceValidator {

    public void validateEmptyGender(AccountRequest.InsureBoarding request) {
        if (request.getGender() != null && request.getGender().trim().length() == 0) {
            throw new CustomException(NOT_EMPTY_GENDER);
        }
    }

    public void validateAlreadyAddInsurance(boolean isInsuranceAlreadyExists) {
        if (isInsuranceAlreadyExists) {
            throw new CustomException(ALREADY_ADD_INSURANCE);
        }
    }
}
