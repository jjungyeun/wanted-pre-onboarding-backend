package com.wjy.wantedpreonboarding.repository;

import com.wjy.wantedpreonboarding.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
