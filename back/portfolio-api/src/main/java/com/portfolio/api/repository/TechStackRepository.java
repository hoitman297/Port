package com.portfolio.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.api.domain.TechStack;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {

    List<TechStack> findAllByOrderByCategoryAscNameAsc();
}
