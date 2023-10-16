package com.wjy.wantedpreonboarding.repository;

import com.wjy.wantedpreonboarding.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
