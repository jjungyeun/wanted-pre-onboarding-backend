package com.wjy.wantedpreonboarding.service;

import com.wjy.wantedpreonboarding.dto.req.RecruitmentCreateDto;
import com.wjy.wantedpreonboarding.dto.req.RecruitmentEditDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentDetailResponseDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruitmentService {
     Long createRecruitment(RecruitmentCreateDto createDto);
     void editRecruitment(Long recruitmentId, RecruitmentEditDto editDto);
     void deleteRecruitment(Long recruitmentId);
     List<RecruitmentSearchResponseDto> searchRecruitment(String query);
     RecruitmentDetailResponseDto getRecruitmentDetail(Long recruitmentId);
     void applyRecruitment(Long recruitmentId, Long memberId);
}
