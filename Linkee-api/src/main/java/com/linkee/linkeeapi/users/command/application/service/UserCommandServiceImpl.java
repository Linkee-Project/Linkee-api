package com.linkee.linkeeapi.users.command.application.service;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.users.command.application.dto.request.UpdateUserRoleRequest;
import com.linkee.linkeeapi.users.command.infrastructure.repository.RelationRepository;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService{

    private final UserRepository userRepository;
    private final RelationRepository relationRepository;


    @Transactional
    @Override
    public void updateNickname(Long userId ,String newNickName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        if(user.getUserStatus() == Status.N){
            throw new BusinessException(ErrorCode.INVALID_USER_ID,"탈퇴한 회원입니다.");
        }

        user.modifyUserNickName(newNickName);
    }


    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        user.deactivateUser();

        // relation에 receiver 나 requester 정보있으면 삭제
        relationRepository.deleteByReceiverOrRequester(user,user);

    }

    @Override
    public void updateUserRole(UpdateUserRoleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        user.changeUserRole(request.getNewRole());
    }


}
