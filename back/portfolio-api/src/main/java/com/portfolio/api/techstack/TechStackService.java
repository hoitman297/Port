package com.portfolio.api.techstack;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.api.domain.TechStack;
import com.portfolio.api.exception.ResourceNotFoundException;
import com.portfolio.api.exception.TechStackInUseException;
import com.portfolio.api.repository.ProjectTechStackRepository;
import com.portfolio.api.repository.TechStackRepository;
import com.portfolio.api.techstack.dto.TechStackRequest;
import com.portfolio.api.techstack.dto.TechStackResponse;

@Service
@Transactional(readOnly = true)
public class TechStackService {

    private final TechStackRepository techStackRepository;
    private final ProjectTechStackRepository projectTechStackRepository;

    public TechStackService(TechStackRepository techStackRepository, ProjectTechStackRepository projectTechStackRepository) {
        this.techStackRepository = techStackRepository;
        this.projectTechStackRepository = projectTechStackRepository;
    }

    public List<TechStackResponse> getAll() {
        return techStackRepository.findAllByOrderByCategoryAscNameAsc().stream()
            .map(TechStackResponse::from)
            .toList();
    }

    @Transactional
    public TechStackResponse create(TechStackRequest request) {
        TechStack saved = techStackRepository.save(new TechStack(request.name(), request.category()));
        return TechStackResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!techStackRepository.existsById(id)) {
            throw new ResourceNotFoundException("기술 스택을 찾을 수 없습니다.");
        }

        if (projectTechStackRepository.existsByTechStackId(id)) {
            List<Long> usedByProjectIds = projectTechStackRepository.findProjectIdsByTechStackId(id);
            throw new TechStackInUseException(usedByProjectIds);
        }

        techStackRepository.deleteById(id);
    }
}
