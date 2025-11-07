package com.linkee.linkeeapi.common.security.model;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;


// 스프링 프레임워크의 User는 username , password , authorities 의 정보 밖에 가져오지 못한다
// 때문에 다른 값들을 가져오고 싶으면 User 를 상속받은 CustomUser 를 정의해서 userId값을 받아올 수 있게 만든다
public class CustomUser extends User {

    private final Long userId;

    public CustomUser(Long userId,
                      String username,
                      String password,
                      String authorities) {

        super(username, password, List.of(new SimpleGrantedAuthority(authorities)));
        this.userId = userId;

    }

    public Long getUserId() {
        return userId;
    }

}
