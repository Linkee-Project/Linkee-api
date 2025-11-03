package com.linkee.linkeeapi.alarm_template.command.domain.aggregate.entity;

import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Entity
@Table(name = "tb_alarm_template")
public class AlarmTemplate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;
    private String templateContent;

    public void modifyTemplateContent(String content) {
        this.templateContent = content;
    }
}
