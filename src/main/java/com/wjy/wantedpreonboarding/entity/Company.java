package com.wjy.wantedpreonboarding.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String region;

    @OneToMany(mappedBy = "company")
    private List<Recruitment> recruitments = new ArrayList<>();

    @Builder
    public Company(String name, String country, String region) {
        this.name = name;
        this.country = country;
        this.region = region;
    }
}
