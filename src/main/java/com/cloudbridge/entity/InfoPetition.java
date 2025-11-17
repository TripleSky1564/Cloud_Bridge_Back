package com.cloudbridge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "INFO_PETTION")
public class InfoPetition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INFO_ID")
    private Long id;

    @Column(name = "CP_INFO_ID", nullable = false, length = 50)
    private String cpInfoId;

    @Column(name = "CP_DESCRIPTION", nullable = false, columnDefinition = "text")
    private String description;
}
