package com.wjy.wantedpreonboarding.dto.res;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RecruitmentDetailResponseDto {
    private Long recruitmentId;
    private String companyName;
    private String country;
    private String region;
    private String position;
    private int reward;
    private String skill;
    private String contents;
    private List<Long> otherRecruitments;
}
