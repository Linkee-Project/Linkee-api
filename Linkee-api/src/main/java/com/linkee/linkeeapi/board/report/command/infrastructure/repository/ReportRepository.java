package com.linkee.linkeeapi.board.report.command.infrastructure.repository;


import com.linkee.linkeeapi.board.report.command.domain.aggregate.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
