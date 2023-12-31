package com.wjy.wantedpreonboarding.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentCreateDto {
    private Long companyId;
    private String position;
    private int reward;
    private String contents;
    private String skill;
}
