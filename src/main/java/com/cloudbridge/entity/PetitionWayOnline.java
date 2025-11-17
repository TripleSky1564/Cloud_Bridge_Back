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
@Table(name = "PETITION_WAY_ONLINE")
public class PetitionWayOnline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WAY_ID")
    private Long id;

    @Column(name = "CP_INFO_ID", nullable = false, length = 50)
    private String cpInfoId;

    @Column(name = "ONLINE", nullable = false, columnDefinition = "text")
    private String step;
}
