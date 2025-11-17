package com.cloudbridge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CIVIL_PETITION_INFO")
@Getter
@Setter
@NoArgsConstructor
public class MainCivilPetition {

    @Id
    @Column(name = "CP_INFO_ID", nullable = false, length = 50)
    private String infoId;

    @Column(name = "CP_NAME", length = 100, nullable = false)
    private String cpName;

    @Column(name = "CP_SIMPLE", length = 4000, nullable = false)
    private String simple;
}
