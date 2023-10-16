package com.wjy.wantedpreonboarding.service.impl;

import com.wjy.wantedpreonboarding.dto.req.RecruitmentCreateDto;
import com.wjy.wantedpreonboarding.dto.req.RecruitmentEditDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentDetailResponseDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentSearchResponseDto;
import com.wjy.wantedpreonboarding.entity.Company;
import com.wjy.wantedpreonboarding.entity.Recruitment;
import com.wjy.wantedpreonboarding.exception.custom.CompanyNotFoundException;
import com.wjy.wantedpreonboarding.exception.custom.RecruitmentNotFoundException;
import com.wjy.wantedpreonboarding.repository.CompanyApplicationRepository;
import com.wjy.wantedpreonboarding.repository.CompanyRepository;
import com.wjy.wantedpreonboarding.repository.MemberRepository;
import com.wjy.wantedpreonboarding.repository.RecruitmentRepository;
import com.wjy.wantedpreonboarding.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentServiceImpl implements RecruitmentService {

    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final CompanyApplicationRepository applicationRepository;

    @Override
    @Transactional
    public Long createRecruitment(RecruitmentCreateDto createDto) {
        Company company = companyRepository.findById(createDto.getCompanyId())
                .orElseThrow(CompanyNotFoundException::new);

        Recruitment newRecruitment = recruitmentRepository.save(
                Recruitment.builder()
                        .company(company)
                        .position(createDto.getPosition())
                        .reward(createDto.getReward())
                        .contents(createDto.getContents())
                        .skill(createDto.getSkill())
                        .build());
        return newRecruitment.getId();
    }

    @Override
    @Transactional
    public void editRecruitment(Long recruitmentId, RecruitmentEditDto editDto) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);

        recruitment.editDetail(editDto.getPosition(), editDto.getReward(),
                editDto.getContents(),editDto.getSkill());
    }

    @Override
    @Transactional
    public void deleteRecruitment(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);
        recruitmentRepository.delete(recruitment);
    }

    @Override
    public Page<RecruitmentSearchResponseDto> searchRecruitment(String query, Pageable pageable) {
        return null;
    }

    @Override
    public RecruitmentDetailResponseDto getRecruitmentDetail(Long recruitmentId) {
        return null;
    }

    @Override
    @Transactional
    public void applyRecruitment(Long recruitmentId, Long memberId) {

    }
}
