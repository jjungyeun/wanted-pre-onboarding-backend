package com.wjy.wantedpreonboarding.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.wjy.wantedpreonboarding.dto.req.RecruitmentApplyDto;
import com.wjy.wantedpreonboarding.dto.req.RecruitmentCreateDto;
import com.wjy.wantedpreonboarding.dto.req.RecruitmentEditDto;
import com.wjy.wantedpreonboarding.entity.Company;
import com.wjy.wantedpreonboarding.entity.CompanyApplication;
import com.wjy.wantedpreonboarding.entity.Member;
import com.wjy.wantedpreonboarding.entity.Recruitment;
import com.wjy.wantedpreonboarding.exception.ErrorCode;
import com.wjy.wantedpreonboarding.repository.CompanyApplicationRepository;
import com.wjy.wantedpreonboarding.repository.CompanyRepository;
import com.wjy.wantedpreonboarding.repository.MemberRepository;
import com.wjy.wantedpreonboarding.repository.RecruitmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class RecruitmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RecruitmentRepository recruitmentRepository;
    @Autowired
    private CompanyApplicationRepository applicationRepository;

    @Test
    @DisplayName("채용공고를 추가한다.")
    public void createRecruitment() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());

        String position = "시니어 BE 개발자";
        int reward = 3000000;
        String contents = "원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..";
        String skill = "Java & Spring";

        RecruitmentCreateDto createDto = RecruitmentCreateDto.builder()
                .companyId(company.getId())
                .position(position)
                .reward(reward)
                .contents(contents)
                .skill(skill)
                .build();

        // when & then
        MvcResult result = mockMvc.perform(
                        post("/recruitments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.created").isNumber())
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer createdId = JsonPath.parse(response).read("$.created");

        Recruitment createdRec = recruitmentRepository.findById(Long.valueOf(createdId)).get();
        Assertions.assertEquals(position, createdRec.getPosition());
        Assertions.assertEquals(reward, createdRec.getReward());
        Assertions.assertEquals(contents, createdRec.getContents());
        Assertions.assertEquals(skill, createdRec.getSkill());
    }

    @Test
    @DisplayName("회사 정보가 존재하지 않아 채용공고를 추가하는 데 실패한다.")
    public void createRecruitment_noCompany() throws Exception {
        // given
        String position = "시니어 BE 개발자";
        int reward = 3000000;
        String contents = "원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..";
        String skill = "Java & Spring";

        RecruitmentCreateDto createDto = RecruitmentCreateDto.builder()
                .companyId(1234L)
                .position(position)
                .reward(reward)
                .contents(contents)
                .skill(skill)
                .build();

        // when & then
        mockMvc.perform(
                        post("/recruitments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.COMPANY_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("채용공고를 수정한다.")
    public void editRecruitment() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());
        Recruitment recruitment = recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("시니어 BE 개발자")
                .reward(3000000)
                .contents("원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..")
                .skill("Java & Spring")
                .build());

        RecruitmentEditDto editDto = RecruitmentEditDto.builder()
                .position("백엔드 주니어 개발자")
                .contents("원티드랩에서 백엔드 주니어 개발자를 채용합니다.")
                .build();

        // when & then
        mockMvc.perform(
                        patch("/recruitments/{recruitmentId}", recruitment.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(editDto))
                )
                .andExpect(status().isOk())
                .andDo(print());

        Recruitment createdRec = recruitmentRepository.findById(recruitment.getId()).get();
        Assertions.assertEquals("백엔드 주니어 개발자", createdRec.getPosition());
        Assertions.assertEquals(3000000, createdRec.getReward());
        Assertions.assertEquals("원티드랩에서 백엔드 주니어 개발자를 채용합니다.", createdRec.getContents());
        Assertions.assertEquals("Java & Spring", createdRec.getSkill());
    }

    @Test
    @DisplayName("채용공고가 존재하지 않아 수정에 실패한다.")
    public void editRecruitment_noRecruitment() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());

        RecruitmentEditDto editDto = RecruitmentEditDto.builder()
                .position("백엔드 주니어 개발자")
                .contents("원티드랩에서 백엔드 주니어 개발자를 채용합니다.")
                .build();

        // when & then
        mockMvc.perform(
                        patch("/recruitments/{recruitmentId}", 1234L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(editDto))
                )
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.RECRUITMENT_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("채용공고를 삭제한다.")
    public void deleteRecruitment() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());
        Recruitment recruitment = recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("시니어 BE 개발자")
                .reward(3000000)
                .contents("원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..")
                .skill("Java & Spring")
                .build());

        // when & then
        mockMvc.perform(delete("/recruitments/{recruitmentId}", recruitment.getId()))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertFalse(recruitmentRepository.findById(recruitment.getId()).isPresent());
    }

    @Test
    @DisplayName("채용공고가 존재하지 않아 삭제에 실패한다.")
    public void deleteRecruitment_noRecruitment() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());

        // when & then
        mockMvc.perform(delete("/recruitments/{recruitmentId}", 1234L))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.RECRUITMENT_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("채용공고 정보를 조회한다.")
    public void getRecruitmentDetail() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());
        Recruitment recruitment = recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("시니어 BE 개발자")
                .reward(3000000)
                .contents("원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..")
                .skill("Java & Spring")
                .build());
        recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("백엔드 주니어 개발자 (Python)")
                .reward(1000000)
                .contents("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                .skill("Python")
                .build());
        recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("백엔드 주니어 개발자 (Java)")
                .reward(1000000)
                .contents("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                .skill("Java & Spring")
                .build());

        // when & then
        mockMvc.perform(get("/recruitments/{recruitmentId}", recruitment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recruitmentId").value(recruitment.getId()))
                .andExpect(jsonPath("$.companyName").value(company.getName()))
                .andExpect(jsonPath("$.country").value(company.getCountry()))
                .andExpect(jsonPath("$.region").value(company.getRegion()))
                .andExpect(jsonPath("$.position").value(recruitment.getPosition()))
                .andExpect(jsonPath("$.reward").value(recruitment.getReward()))
                .andExpect(jsonPath("$.skill").value(recruitment.getSkill()))
                .andExpect(jsonPath("$.contents").value(recruitment.getContents()))
                .andDo(print());
    }

    @Test
    @DisplayName("채용공고가 존재하지 않아 조회에 실패한다.")
    public void getRecruitmentDetail_noRecruitment() throws Exception {
        // when & then
        mockMvc.perform(
                        get("/recruitments/{recruitmentId}", 1234L)
                )
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.RECRUITMENT_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("채용공고 목록을 검색한다.")
    public void searchRecruitment() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());
        Company company2 = companyRepository.save(Company.builder()
                .name("네이버")
                .country("한국")
                .region("정자")
                .build());
        Recruitment recruitment = recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("시니어 BE 개발자")
                .reward(3000000)
                .contents("원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..")
                .skill("Java & Spring")
                .build());
        recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("백엔드 주니어 개발자 (Python)")
                .reward(1000000)
                .contents("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                .skill("Python")
                .build());
        recruitmentRepository.save(Recruitment.builder()
                .company(company2)
                .position("백엔드 주니어 개발자 (Java)")
                .reward(1000000)
                .contents("백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                .skill("Java & Spring")
                .build());

        // when & then - "시니어" 검색
        mockMvc.perform(get("/recruitments?search={search}", "시니어"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recruitmentId").value(recruitment.getId()))
                .andExpect(jsonPath("$[0].companyName").value(company.getName()))
                .andExpect(jsonPath("$[0].country").value(company.getCountry()))
                .andExpect(jsonPath("$[0].region").value(company.getRegion()))
                .andExpect(jsonPath("$[0].position").value(recruitment.getPosition()))
                .andExpect(jsonPath("$[0].reward").value(recruitment.getReward()))
                .andExpect(jsonPath("$[0].skill").value(recruitment.getSkill()))
                .andDo(print());

        // when & then - 검색어 X
        long recCnt = recruitmentRepository.count();
        mockMvc.perform(get("/recruitments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(recCnt))
                .andDo(print());
    }

    @Test
    @DisplayName("채용공고에 지원한다.")
    public void applyRecruitment() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());
        Recruitment recruitment = recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("시니어 BE 개발자")
                .reward(3000000)
                .contents("원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..")
                .skill("Java & Spring")
                .build());
        Member member = memberRepository.save(Member.builder()
                .name("김철수")
                .build());

        RecruitmentApplyDto applyDto = new RecruitmentApplyDto(member.getId());

        // when & then
        mockMvc.perform(post("/recruitments/{recruitmentId}", recruitment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(applyDto))
                )
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertTrue(
                applicationRepository.findByRecruitmentAndMember(recruitment, member).isPresent());
    }

    @Test
    @DisplayName("이미 지원한 공고에 다시 지원할 수 없다.")
    public void applyRecruitment_duplicate() throws Exception {
        // given
        Company company = companyRepository.save(Company.builder()
                .name("원티드랩")
                .country("한국")
                .region("서울")
                .build());
        Recruitment recruitment = recruitmentRepository.save(Recruitment.builder()
                .company(company)
                .position("시니어 BE 개발자")
                .reward(3000000)
                .contents("원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..")
                .skill("Java & Spring")
                .build());
        Member member = memberRepository.save(Member.builder()
                .name("김철수")
                .build());
        applicationRepository.save(CompanyApplication.builder()
                .recruitment(recruitment)
                .member(member)
                .build());

        RecruitmentApplyDto applyDto = new RecruitmentApplyDto(member.getId());

        // when & then
        mockMvc.perform(post("/recruitments/{recruitmentId}", recruitment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(applyDto))
                )
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.CANNOT_APPLY_MULTIPLE_TIMES.getMessage()))
                .andDo(print());

        Assertions.assertTrue(
                applicationRepository.findByRecruitmentAndMember(recruitment, member).isPresent());
    }

}