package com.wjy.wantedpreonboarding.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private int reward;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String skill;

    @Builder
    public Recruitment(Company company, String position, int reward, String contents, String skill) {
        this.company = company;
        this.position = position;
        this.reward = reward;
        this.contents = contents;
        this.skill = skill;
    }

    public void editDetail(String position, Integer reward, String contents, String skill) {
        if (position != null && !position.isBlank()) this.position = position;
        if (reward != null) this.reward = reward;
        if (contents != null && !contents.isBlank()) this.contents = contents;
        if (skill != null && !skill.isBlank()) this.skill = skill;
    }
}
