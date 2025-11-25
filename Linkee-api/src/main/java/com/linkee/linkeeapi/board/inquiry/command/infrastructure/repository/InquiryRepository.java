package com.linkee.linkeeapi.board.inquiry.command.infrastructure.repository;

import com.linkee.linkeeapi.board.inquiry.command.domain.aggregate.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}

