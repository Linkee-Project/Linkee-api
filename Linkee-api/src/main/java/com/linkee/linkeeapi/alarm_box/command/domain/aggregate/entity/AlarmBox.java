package com.linkee.linkeeapi.alarm_box.command.domain.aggregate.entity;

import com.linkee.linkeeapi.common.enums.Status;
import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import com.linkee.linkeeapi.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_alarm_box")
public class AlarmBox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long alarmBoxId;
    private String alarmBoxContent;

    @Enumerated(EnumType.STRING)
    private Status isChecked = Status.N;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;




    public void checkedAlarm(){
        this.isChecked = Status.Y;
    }

 }
