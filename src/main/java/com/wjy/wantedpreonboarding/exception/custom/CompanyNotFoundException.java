package com.wjy.wantedpreonboarding.exception.custom;

import com.wjy.wantedpreonboarding.exception.CustomException;
import com.wjy.wantedpreonboarding.exception.ErrorCode;

public class CompanyNotFoundException extends CustomException {
    public CompanyNotFoundException() {
        super(ErrorCode.COMPANY_NOT_FOUND, null);
    }
}
