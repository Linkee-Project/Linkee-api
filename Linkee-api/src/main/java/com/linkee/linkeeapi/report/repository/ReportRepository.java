package com.linkee.linkeeapi.report.repository;

import com.linkee.linkeeapi.report.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
