package com.portfolio.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.api.domain.Troubleshooting;

public interface TroubleshootingRepository extends JpaRepository<Troubleshooting, Long> {
}
