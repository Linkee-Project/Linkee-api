package com.linkee.linkeeapi.users.query.dto.response;

import com.linkee.linkeeapi.users.command.domain.entity.UserGrade;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeResponse {

    private String userEmail;
    private String userNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private List<UserGrade> userGrades; // 본인의 등급 리스트

}


