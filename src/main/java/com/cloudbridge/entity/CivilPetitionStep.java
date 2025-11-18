package com.cloudbridge.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CIVIL_PETITION_STEP")
@Getter
@NoArgsConstructor
public class CivilPetitionStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STEP_ID")
    private Long stepId;

    @Column(name = "CP_INFO_ID", nullable = false, length = 50)
    private String cpInfoId;

    @Column(name = "STEP_ORDER", nullable = false)
    private Integer stepOrder;

    @Column(name = "STEP_MODE", nullable = false)
    private Integer stepMode;

    @Column(name = "CONTENT", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "LINK_URL")
    private String linkUrl;

    @Column(name = "CATEGORY", length = 50)
    private String category;

    @ManyToMany
    @JoinTable(
            name = "CIVIL_PETITION_STEP_INSTITUTION",
            joinColumns = @JoinColumn(name = "STEP_ID"),
            inverseJoinColumns = @JoinColumn(name = "INSTITUTION_ID")
    )
    private List<Institution> institutions = new ArrayList<>();
}
