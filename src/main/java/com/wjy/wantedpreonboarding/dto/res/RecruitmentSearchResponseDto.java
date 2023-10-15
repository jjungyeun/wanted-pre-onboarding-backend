package com.wjy.wantedpreonboarding.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruitmentSearchResponseDto {
    private Long recruitmentId;
    private String companyName;
    private String country;
    private String region;
    private String position;
    private int reward;
    private String skill;
}
