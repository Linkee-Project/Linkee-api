package com.linkee.linkeeapi.chat.command.application.dto.request;

import com.linkee.linkeeapi.chat.command.domain.aggregate.entity.ChatRoomType;
import com.linkee.linkeeapi.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomCreateRequestDto {
    @NotBlank
    private String chatRoomName;

    @NotNull
    private ChatRoomType chatRoomType;

    private Status isPrivate;

    private Integer roomCode;

    @NotNull
    private Long roomOwnerId;

    private Integer roomCapacity; //게임방의 경우 최대인원 5명

}
