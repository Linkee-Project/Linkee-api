package com.linkee.linkeeapi.board.notice.command.infrastructure.repository;

import com.linkee.linkeeapi.board.notice.command.domain.aggregate.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
