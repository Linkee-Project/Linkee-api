package com.linkee.linkeeapi.inquiry.command.infrastructure.repository;

import com.linkee.linkeeapi.inquiry.command.domain.aggregate.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInquiryRepository extends JpaRepository<Inquiry, Long> {

}
