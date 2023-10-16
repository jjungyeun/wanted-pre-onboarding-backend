package com.wjy.wantedpreonboarding.exception.custom;

import com.wjy.wantedpreonboarding.exception.CustomException;
import com.wjy.wantedpreonboarding.exception.ErrorCode;

public class RecruitmentNotFoundException extends CustomException {
    public RecruitmentNotFoundException() {
        super(ErrorCode.RECRUITMENT_NOT_FOUND, null);
    }
}
