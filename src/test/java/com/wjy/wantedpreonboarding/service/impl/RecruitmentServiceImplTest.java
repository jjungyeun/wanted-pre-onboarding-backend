package com.wjy.wantedpreonboarding.service.impl;

import com.wjy.wantedpreonboarding.dto.req.RecruitmentCreateDto;
import com.wjy.wantedpreonboarding.entity.Company;
import com.wjy.wantedpreonboarding.entity.Recruitment;
import com.wjy.wantedpreonboarding.exception.custom.CompanyNotFoundException;
import com.wjy.wantedpreonboarding.repository.CompanyApplicationRepository;
import com.wjy.wantedpreonboarding.repository.CompanyRepository;
import com.wjy.wantedpreonboarding.repository.MemberRepository;
import com.wjy.wantedpreonboarding.repository.RecruitmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceImplTest {
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RecruitmentRepository recruitmentRepository;
    @Mock
    private CompanyApplicationRepository applicationRepository;
    @InjectMocks
    private RecruitmentServiceImpl recruitmentService;

    @Test
    @DisplayName("채용공고를 추가한다.")
    public void createRecruitment() {
        // given
        Long companyId = 1L;
        String position = "백엔드 주니어 개발자";
        int reward = 1000000;
        String contents = "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..";
        String skill = "Python";

        RecruitmentCreateDto createDto = RecruitmentCreateDto.builder()
                .companyId(companyId)
                .position(position)
                .reward(reward)
                .contents(contents)
                .skill(skill)
                .build();

        Company company = mock(Company.class);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        Long createdId = 10L;
        Recruitment recruitment = mock(Recruitment.class);
        when(recruitment.getId()).thenReturn(createdId);
        when(recruitmentRepository.save(any(Recruitment.class))).thenReturn(recruitment);

        // when
        Long resultId = recruitmentService.createRecruitment(createDto);

        // then
        Assertions.assertEquals(createdId, resultId);
    }

    @Test
    @DisplayName("회사 정보가 존재하지 않아 채용공고를 추가하는 데 실패한다.")
    public void createRecruitment_noCompany() {
        // given
        Long companyId = 1L;
        String position = "백엔드 주니어 개발자";
        int reward = 1000000;
        String contents = "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..";
        String skill = "Python";

        RecruitmentCreateDto createDto = RecruitmentCreateDto.builder()
                .companyId(companyId)
                .position(position)
                .reward(reward)
                .contents(contents)
                .skill(skill)
                .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(CompanyNotFoundException.class, () -> recruitmentService.createRecruitment(createDto));
    }
}