package com.wjy.wantedpreonboarding.repository;

import com.wjy.wantedpreonboarding.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    @Query("select r from Recruitment r join fetch r.company")
    List<Recruitment> findAllWithCompany();
}
