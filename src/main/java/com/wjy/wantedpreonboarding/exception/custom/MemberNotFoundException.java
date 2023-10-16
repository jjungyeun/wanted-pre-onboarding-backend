package com.wjy.wantedpreonboarding.exception.custom;

import com.wjy.wantedpreonboarding.exception.CustomException;
import com.wjy.wantedpreonboarding.exception.ErrorCode;

public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND, null);
    }
}
