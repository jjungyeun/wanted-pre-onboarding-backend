package com.wjy.wantedpreonboarding.service.impl;

import com.wjy.wantedpreonboarding.dto.req.RecruitmentCreateDto;
import com.wjy.wantedpreonboarding.dto.req.RecruitmentEditDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentDetailResponseDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentSearchResponseDto;
import com.wjy.wantedpreonboarding.entity.Company;
import com.wjy.wantedpreonboarding.entity.CompanyApplication;
import com.wjy.wantedpreonboarding.entity.Member;
import com.wjy.wantedpreonboarding.entity.Recruitment;
import com.wjy.wantedpreonboarding.exception.custom.CannotApplyMultipleTimesException;
import com.wjy.wantedpreonboarding.exception.custom.CompanyNotFoundException;
import com.wjy.wantedpreonboarding.exception.custom.MemberNotFoundException;
import com.wjy.wantedpreonboarding.exception.custom.RecruitmentNotFoundException;
import com.wjy.wantedpreonboarding.repository.CompanyApplicationRepository;
import com.wjy.wantedpreonboarding.repository.CompanyRepository;
import com.wjy.wantedpreonboarding.repository.MemberRepository;
import com.wjy.wantedpreonboarding.repository.RecruitmentRepository;
import com.wjy.wantedpreonboarding.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public List<RecruitmentSearchResponseDto> searchRecruitment(String query) {
        return recruitmentRepository.findAllWithCompany().stream()
                .map(it -> RecruitmentSearchResponseDto.builder()
                        .recruitmentId(it.getId())
                        .position(it.getPosition())
                        .reward(it.getReward())
                        .skill(it.getSkill())
                        .companyName(it.getCompany().getName())
                        .country(it.getCompany().getCountry())
                        .region(it.getCompany().getRegion())
                        .build())
                .filter(it -> isSearchResult(it, query))
                .toList();
    }

    private boolean isSearchResult(RecruitmentSearchResponseDto recruitment, String query) {
        return query == null || query.isBlank()
                || recruitment.getCompanyName().contains(query)
                || recruitment.getCountry().contains(query)
                || recruitment.getRegion().contains(query)
                || recruitment.getPosition().contains(query)
                || recruitment.getSkill().contains(query);
    }

    @Override
    public RecruitmentDetailResponseDto getRecruitmentDetail(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);

        Company company = recruitment.getCompany();
        List<Long> otherRecruitmentIds = company.getRecruitments().stream()
                .map(Recruitment::getId)
                .filter(id -> !Objects.equals(id, recruitmentId))
                .toList();

        return RecruitmentDetailResponseDto.builder()
                .recruitmentId(recruitmentId)
                .companyName(company.getName())
                .country(company.getCountry())
                .region(company.getRegion())
                .position(recruitment.getPosition())
                .reward(recruitment.getReward())
                .skill(recruitment.getSkill())
                .contents(recruitment.getContents())
                .otherRecruitments(otherRecruitmentIds)
                .build();
    }

    @Override
    @Transactional
    public void applyRecruitment(Long recruitmentId, Long memberId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        if (isAlreadyApplied(recruitment, member)) {
            throw new CannotApplyMultipleTimesException();
        }

        applicationRepository.save(
                CompanyApplication.builder()
                        .recruitment(recruitment)
                        .member(member)
                        .build()
        );
    }

    private boolean isAlreadyApplied(Recruitment recruitment, Member member) {
        Optional<CompanyApplication> applicationOptional =
                applicationRepository.findByRecruitmentAndMember(recruitment, member);
        return applicationOptional.isPresent();
    }
}
