package com.cloudbridge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MY_CASE")
@Getter
@Setter
@NoArgsConstructor
public class MyCase {

    @Id
    @Column(name = "CASE_ID", nullable = false, length = 50)
    private String caseId;

    @Column(name = "MEMBER_ID", nullable = false, length = 50)
    private String memberId;

    @Column(name = "CP_INFO_ID", nullable = false, length = 50)
    private String cpInfoId;

    @Column(name = "STATUS", length = 20)
    private String status = "in-progress";

    @Column(name = "CHECKLIST", columnDefinition = "JSON")
    private String checklist;

    @Column(name = "STARTED_AT")
    private LocalDateTime startedAt;

    @Column(name = "COMPLETED_AT")
    private LocalDateTime completedAt;
}
