package com.linkee.linkeeapi.common.config;

import com.linkee.linkeeapi.user.model.dto.UserCreateRequest;
import com.linkee.linkeeapi.user.model.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /* ModelMapper : 런타임 시 객체 간의 매핑을 자동화 해주는 라이브러리 */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                /* 필드명이 온전히 같을 경우에만 매핑 */
//                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                /* Entity의 Setter 메소드 미사용으로 필드 접근 허용 */
                .setFieldAccessLevel(
                        org.modelmapper.config.Configuration.AccessLevel.PRIVATE
                )
                .setFieldMatchingEnabled(true);

        // UserCreateRequest -> User 맵핑 시 userId 필드는 건너뛰도록 설행
        modelMapper.typeMap(UserCreateRequest.class, User.class)
                .addMappings(mapper
                        -> {mapper.skip(User::setUserId);
                });

        return modelMapper;
    }
}
