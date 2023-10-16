package com.wjy.wantedpreonboarding.exception.custom;

import com.wjy.wantedpreonboarding.exception.CustomException;
import com.wjy.wantedpreonboarding.exception.ErrorCode;

public class CannotApplyMultipleTimesException extends CustomException {
    public CannotApplyMultipleTimesException() {
        super(ErrorCode.CANNOT_APPLY_MULTIPLE_TIMES, null);
    }
}
