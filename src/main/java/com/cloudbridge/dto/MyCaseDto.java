package com.cloudbridge.dto;

import com.cloudbridge.entity.MyCase;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

public class MyCaseDto {

    @Getter
    @Setter
    public static class Request {
        private String memberId;
        private String cpInfoId;
        private String status;
        private String checklist;
    }

    @Getter
    public static class Response {
        private String caseId;
        private String memberId;
        private String cpInfoId;
        private String status;
        private String checklist;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;

        public Response(MyCase entity) {
            this.caseId = entity.getCaseId();
            this.memberId = entity.getMemberId();
            this.cpInfoId = entity.getCpInfoId();
            this.status = entity.getStatus();
            this.checklist = entity.getChecklist();
            this.startedAt = entity.getStartedAt();
            this.completedAt = entity.getCompletedAt();
        }
    }
}
