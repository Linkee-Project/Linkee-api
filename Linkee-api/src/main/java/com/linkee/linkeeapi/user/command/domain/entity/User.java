package com.linkee.linkeeapi.user.command.domain.entity;

import com.linkee.linkeeapi.common.enums.Role;
import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
        name = "tb_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_user_email", columnNames = "user_email"),
                @UniqueConstraint(name = "uk_user_nickname", columnNames = "user_nickname")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "user_password", nullable = false, length = 300)
    private String userPassword;

    @Column(name = "user_nickname", nullable = false, length = 10)
    private String userNickname;

    //네이버 로그인 id를 받아오기 위한 칼럼 추가
    @Builder.Default
    @Column(name = "user_login_id", nullable = false, length = 50)
    private String userLoginId = "";


    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    @Builder.Default
    private Status userStatus = Status.Y;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    @Builder.Default
    private Role userRole = Role.USER;


    // 관리자 회원가입용
    public static User createAdminUser(String email, String password, String nickname) {
        return User.builder()
                .userEmail(email)
                .userPassword(password)
                .userNickname(nickname)
                .userRole(Role.ADMIN)
                .userStatus(Status.Y)
                .build();
    }

    // 일반 회원가입용
    public static User createNormalUser(String email, String password, String nickname) {
        return User.builder()
                .userEmail(email)
                .userPassword(password)
                .userNickname(nickname)
                .userRole(Role.USER)
                .userStatus(Status.Y)
                .build();
    }

    // 소셜 로그인용
    public static User createSocialUser(String loginId, String email, String nickname, Role userRole) {
        return User.builder()
                .userLoginId(loginId)
                .userEmail(email)
                .userNickname(nickname)
                .userPassword("")  // 소셜 로그인은 비밀번호 없음
                .userRole(userRole)
                .userStatus(Status.Y)
                .build();
    }


    public void modifyUserNickName(String userNickname){
        this.userNickname = userNickname;
    }


    public void deactivateUser() {
        this.userStatus = Status.N;
    }

    public void changePassword(String encode) {
        this.userPassword = encode;
    }
}
