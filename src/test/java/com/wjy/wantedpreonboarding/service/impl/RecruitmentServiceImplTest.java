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
import com.wjy.wantedpreonboarding.exception.custom.RecruitmentNotFoundException;
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

import java.util.List;
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

    @Test
    @DisplayName("채용공고를 수정한다.")
    public void editRecruitment() {
        // given
        Long recruitmentId = 1L;
        String position = "백엔드 주니어 개발자";
        int reward = 1500000;
        String contents = "원티드랩에서 백엔드 주니어 개발자를 채용합니다.";
        String skill = "Django";

        RecruitmentEditDto editDto = RecruitmentEditDto.builder()
                .position(position)
                .reward(reward)
                .contents(contents)
                .skill(skill)
                .build();

        Recruitment recruitment = mock(Recruitment.class);
        when(recruitmentRepository.findById(recruitmentId)).thenReturn(Optional.of(recruitment));

        // when & then
        recruitmentService.editRecruitment(recruitmentId, editDto);
    }

    @Test
    @DisplayName("채용공고가 존재하지 않아 수정에 실패한다.")
    public void editRecruitment_noRecruitment() {
        // given
        Long recruitmentId = 1L;
        String position = "백엔드 주니어 개발자";
        int reward = 1500000;
        String contents = "원티드랩에서 백엔드 주니어 개발자를 채용합니다.";
        String skill = "Django";

        RecruitmentEditDto editDto = RecruitmentEditDto.builder()
                .position(position)
                .reward(reward)
                .contents(contents)
                .skill(skill)
                .build();

        when(recruitmentRepository.findById(recruitmentId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(RecruitmentNotFoundException.class, () ->
                recruitmentService.editRecruitment(recruitmentId, editDto));

    }

    @Test
    @DisplayName("채용공고를 삭제한다.")
    public void deleteRecruitment() {
        // given
        Long recruitmentId = 1L;
        Recruitment recruitment = mock(Recruitment.class);
        when(recruitmentRepository.findById(recruitmentId)).thenReturn(Optional.of(recruitment));

        // when & then
        recruitmentService.deleteRecruitment(recruitmentId);
    }

    @Test
    @DisplayName("채용공고가 존재하지 않아 삭제에 실패한다.")
    public void deleteRecruitment_noRecruitment() {
        // given
        Long recruitmentId = 1L;
        when(recruitmentRepository.findById(recruitmentId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(RecruitmentNotFoundException.class, () ->
                recruitmentService.deleteRecruitment(recruitmentId));

    }

    @Test
    @DisplayName("채용공고 정보를 조회한다.")
    public void getRecruitmentDetail() {
        // given
        Long recruitmentId = 1L;
        String position = "백엔드 주니어 개발자";
        int reward = 1000000;
        String contents = "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..";
        String skill = "Python";
        String companyName = "원티드랩", country = "한국", region = "서울";

        Company company = mock(Company.class);
        when(company.getName()).thenReturn(companyName);
        when(company.getCountry()).thenReturn(country);
        when(company.getRegion()).thenReturn(region);
        Recruitment recruitment = Recruitment.builder()
                .company(company)
                .position(position)
                .reward(reward)
                .contents(contents)
                .skill(skill)
                .build();
        when(recruitmentRepository.findById(recruitmentId)).thenReturn(Optional.of(recruitment));

        // 해당 회사의 다른 공고 추가
        Long recId1 = 10L, recId2 = 11L;
        Recruitment rec1 = mock(Recruitment.class);
        Recruitment rec2 = mock(Recruitment.class);
        when(rec1.getId()).thenReturn(recId1);
        when(rec2.getId()).thenReturn(recId2);
        when(company.getRecruitments()).thenReturn(List.of(rec1, rec2));


        // when
        RecruitmentDetailResponseDto responseDto = recruitmentService.getRecruitmentDetail(recruitmentId);

        // then
        Assertions.assertEquals(recruitmentId, responseDto.getRecruitmentId());
        Assertions.assertEquals(position, responseDto.getPosition());
        Assertions.assertEquals(reward, responseDto.getReward());
        Assertions.assertEquals(contents, responseDto.getContents());
        Assertions.assertEquals(skill, responseDto.getSkill());
        Assertions.assertEquals(companyName, responseDto.getCompanyName());
        Assertions.assertEquals(country, responseDto.getCountry());
        Assertions.assertEquals(region, responseDto.getRegion());
        Assertions.assertTrue(responseDto.getOtherRecruitments().containsAll(List.of(recId1, recId2)));
    }

    @Test
    @DisplayName("채용공고가 존재하지 않아 조회에 실패한다.")
    public void getRecruitmentDetail_noRecruitment() {
        // given
        Long recruitmentId = 1L;
        when(recruitmentRepository.findById(recruitmentId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(RecruitmentNotFoundException.class, () ->
                recruitmentService.getRecruitmentDetail(recruitmentId));

    }

    @Test
    @DisplayName("채용공고 목록을 검색한다.")
    public void searchRecruitment() {
        // given

        // 공고 1 mocking
        Long recruitmentId1 = 1L;
        String position1 = "백엔드 주니어 개발자";
        int reward1 = 1000000;
        String skill1 = "Python";
        String companyName1 = "원티드랩", country1 = "한국", region1 = "서울";

        Company company1 = mock(Company.class);
        when(company1.getName()).thenReturn(companyName1);
        when(company1.getCountry()).thenReturn(country1);
        when(company1.getRegion()).thenReturn(region1);

        Recruitment recruitment1 = mock(Recruitment.class);
        when(recruitment1.getId()).thenReturn(recruitmentId1);
        when(recruitment1.getPosition()).thenReturn(position1);
        when(recruitment1.getReward()).thenReturn(reward1);
        when(recruitment1.getSkill()).thenReturn(skill1);
        when(recruitment1.getCompany()).thenReturn(company1);

        // 공고 2 mocking
        Long recruitmentId2 = 1L;
        String position2 = "프론트엔드 개발자";
        int reward2 = 500000;
        String skill2 = "javascript";
        String companyName2 = "원티드코리아", country2 = "한국", region2 = "부산";

        Company company2 = mock(Company.class);
        when(company2.getName()).thenReturn(companyName2);
        when(company2.getCountry()).thenReturn(country2);
        when(company2.getRegion()).thenReturn(region2);

        Recruitment recruitment2 = mock(Recruitment.class);
        when(recruitment2.getId()).thenReturn(recruitmentId2);
        when(recruitment2.getPosition()).thenReturn(position2);
        when(recruitment2.getReward()).thenReturn(reward2);
        when(recruitment2.getSkill()).thenReturn(skill2);
        when(recruitment2.getCompany()).thenReturn(company2);

        when(recruitmentRepository.findAllWithCompany()).thenReturn(
                List.of(recruitment1, recruitment2)
        );

        // when - 검색어를 입력하지 않은 경우 전체 목록 조회됨
        List<RecruitmentSearchResponseDto> responseDtos = recruitmentService.searchRecruitment(null);

        // then
        Assertions.assertEquals(2, responseDtos.size());

        RecruitmentSearchResponseDto searchRes1 = responseDtos.get(0);
        Assertions.assertEquals(recruitmentId1, searchRes1.getRecruitmentId());
        Assertions.assertEquals(position1, searchRes1.getPosition());
        Assertions.assertEquals(reward1, searchRes1.getReward());
        Assertions.assertEquals(skill1, searchRes1.getSkill());
        Assertions.assertEquals(companyName1, searchRes1.getCompanyName());
        Assertions.assertEquals(country1, searchRes1.getCountry());
        Assertions.assertEquals(region1, searchRes1.getRegion());

        RecruitmentSearchResponseDto searchRes2 = responseDtos.get(1);
        Assertions.assertEquals(recruitmentId2, searchRes2.getRecruitmentId());
        Assertions.assertEquals(position2, searchRes2.getPosition());
        Assertions.assertEquals(reward2, searchRes2.getReward());
        Assertions.assertEquals(skill2, searchRes2.getSkill());
        Assertions.assertEquals(companyName2, searchRes2.getCompanyName());
        Assertions.assertEquals(country2, searchRes2.getCountry());
        Assertions.assertEquals(region2, searchRes2.getRegion());

        // when - 검색어를 입력한 경우 검색어로 필터링됨
        List<RecruitmentSearchResponseDto> responseDtos2 = recruitmentService.searchRecruitment("java");

        // then
        Assertions.assertEquals(1, responseDtos2.size());
        RecruitmentSearchResponseDto searchRes3 = responseDtos2.get(0);
        Assertions.assertEquals(recruitmentId2, searchRes3.getRecruitmentId());
        Assertions.assertEquals(position2, searchRes3.getPosition());
        Assertions.assertEquals(reward2, searchRes3.getReward());
        Assertions.assertEquals(skill2, searchRes3.getSkill());
        Assertions.assertEquals(companyName2, searchRes3.getCompanyName());
        Assertions.assertEquals(country2, searchRes3.getCountry());
        Assertions.assertEquals(region2, searchRes3.getRegion());
    }

    @Test
    @DisplayName("채용공고에 지원한다.")
    public void applyRecruitment() {
        // given
        Long recruitmentId = 1L, memberId = 100L;
        Recruitment recruitment = mock(Recruitment.class);
        Member member = mock(Member.class);

        when(recruitmentRepository.findById(recruitmentId)).thenReturn(Optional.of(recruitment));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(applicationRepository.findByRecruitmentAndMember(recruitment, member))
                .thenReturn(Optional.empty());

        // when & then
        recruitmentService.applyRecruitment(recruitmentId, memberId);
    }

    @Test
    @DisplayName("이미 지원한 공고에 다시 지원할 수 없다.")
    public void applyRecruitment_duplicate() {
        // given
        Long recruitmentId = 1L, memberId = 100L;
        Recruitment recruitment = mock(Recruitment.class);
        Member member = mock(Member.class);
        CompanyApplication application = CompanyApplication.builder()
                .recruitment(recruitment)
                .member(member)
                .build();

        when(recruitmentRepository.findById(recruitmentId)).thenReturn(Optional.of(recruitment));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(applicationRepository.findByRecruitmentAndMember(recruitment, member))
                .thenReturn(Optional.of(application));

        // when & then
        Assertions.assertThrows(CannotApplyMultipleTimesException.class, () -> {
            recruitmentService.applyRecruitment(recruitmentId, memberId);
        });
    }
}