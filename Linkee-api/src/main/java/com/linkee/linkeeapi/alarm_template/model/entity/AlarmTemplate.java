package com.linkee.linkeeapi.alarm_template.model.entity;

import com.linkee.linkeeapi.common.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Entity
public class AlarmTemplate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;
    private String templateContent;

    public void modifyTemplateContent(String content) {
        this.templateContent = content;
    }
}
