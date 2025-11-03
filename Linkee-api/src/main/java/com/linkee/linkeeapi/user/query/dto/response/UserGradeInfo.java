package com.linkee.linkeeapi.user.query.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGradeInfo {
    private String gradeName;
    private String categoryName;
    private int victoryCount;
}
