package com.linkee.linkeeapi.users.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "기존 비밀번호는 필수 입력 값입니다.")
    private String oldPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "새 비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String newPassword;
}
