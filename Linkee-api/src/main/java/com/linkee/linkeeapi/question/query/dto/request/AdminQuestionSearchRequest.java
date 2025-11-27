package com.linkee.linkeeapi.question.query.dto.request;

import com.linkee.linkeeapi.common.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminQuestionSearchRequest {
    private String keyword;
    private String verifiedStr; // Y, N, ALL (null) as String
    private String deletedStr;  // Y, N, ALL (null) as String

    private Integer page;
    private Integer size;
    private Integer offset;

    // Computed getter for Status verified
    public Status getVerified() {
        if (this.verifiedStr == null || this.verifiedStr.trim().isEmpty()) {
            return null; // Represents ALL status
        }
        String upperCaseStr = this.verifiedStr.trim().toUpperCase();
        if ("TRUE".equals(upperCaseStr)) {
            return Status.Y;
        } else if ("FALSE".equals(upperCaseStr)) {
            return Status.N;
        } else {
            try {
                return Status.valueOf(upperCaseStr);
            } catch (IllegalArgumentException e) {
                return null; // Invalid value, treat as ALL
            }
        }
    }

    // Computed getter for Status deleted
    public Status getDeleted() {
        if (this.deletedStr == null || this.deletedStr.trim().isEmpty()) {
            return null; // Represents ALL status
        }
        String upperCaseStr = this.deletedStr.trim().toUpperCase();
        if ("TRUE".equals(upperCaseStr)) {
            return Status.Y;
        } else if ("FALSE".equals(upperCaseStr)) {
            return Status.N;
        } else {
            try {
                return Status.valueOf(upperCaseStr);
            } catch (IllegalArgumentException e) {
                return null; // Invalid value, treat as ALL
            }
        }
    }
}
