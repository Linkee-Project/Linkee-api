package com.linkee.linkeeapi.users.command.application.service;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.exception.BusinessException;
import com.linkee.linkeeapi.common.exception.ErrorCode;
import com.linkee.linkeeapi.users.command.application.dto.request.UpdateUserRoleRequest;
import com.linkee.linkeeapi.users.command.application.dto.request.UpdateUserStatusRequest;
import com.linkee.linkeeapi.users.command.application.dto.request.UpdateUserRoleAndStatusRequest; // Added import
import com.linkee.linkeeapi.users.command.infrastructure.repository.RelationRepository;
import com.linkee.linkeeapi.users.command.domain.entity.User;
import com.linkee.linkeeapi.users.command.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Added SLF4J import
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j // Added SLF4J annotation
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

    @Transactional
    @Override
    public void updateUserRole(UpdateUserRoleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        user.changeUserRole(request.getNewRole());
    }

    @Transactional
    @Override
    public void updateUserStatus(UpdateUserStatusRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        user.updateStatus(request.getStatus());
    }

    @Transactional
    @Override
    public void updateUserRoleAndStatus(UpdateUserRoleAndStatusRequest request) {
        log.info("updateUserRoleAndStatus called for userId: {} with request: {}", request.getUserId(), request); // Added log statement
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_USER_ID));

        if (request.getNewRole() != null) {
            user.changeUserRole(request.getNewRole());
        }
        if (request.getStatus() != null) {
            user.updateStatus(request.getStatus());
        }
    }


}
