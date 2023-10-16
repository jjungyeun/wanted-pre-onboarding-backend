package com.wjy.wantedpreonboarding.repository;

import com.wjy.wantedpreonboarding.entity.CompanyApplication;
import com.wjy.wantedpreonboarding.entity.Member;
import com.wjy.wantedpreonboarding.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyApplicationRepository extends JpaRepository<CompanyApplication, Long> {
    Optional<CompanyApplication> findByRecruitmentAndMember(Recruitment recruitment, Member member);
}
