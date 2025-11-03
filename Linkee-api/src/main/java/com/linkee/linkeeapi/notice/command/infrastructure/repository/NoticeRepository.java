package com.linkee.linkeeapi.notice.command.infrastructure.repository;

import com.linkee.linkeeapi.notice.command.domain.aggregate.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
