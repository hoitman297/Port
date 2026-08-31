package com.portfolio.api.troubleshooting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.api.domain.ProjectFeature;
import com.portfolio.api.domain.Troubleshooting;
import com.portfolio.api.exception.ResourceNotFoundException;
import com.portfolio.api.exception.TroubleshootingAlreadyExistsException;
import com.portfolio.api.project.dto.TroubleshootingResponse;
import com.portfolio.api.repository.ProjectFeatureRepository;
import com.portfolio.api.repository.TroubleshootingRepository;
import com.portfolio.api.troubleshooting.dto.TroubleshootingRequest;

@Service
@Transactional(readOnly = true)
public class TroubleshootingService {

    private final TroubleshootingRepository troubleshootingRepository;
    private final ProjectFeatureRepository projectFeatureRepository;

    public TroubleshootingService(
        TroubleshootingRepository troubleshootingRepository,
        ProjectFeatureRepository projectFeatureRepository
    ) {
        this.troubleshootingRepository = troubleshootingRepository;
        this.projectFeatureRepository = projectFeatureRepository;
    }

    @Transactional
    public TroubleshootingResponse create(Long featureId, TroubleshootingRequest request) {
        ProjectFeature feature = projectFeatureRepository.findById(featureId)
            .orElseThrow(() -> new ResourceNotFoundException("기능을 찾을 수 없습니다."));

        if (feature.getTroubleshooting() != null) {
            throw new TroubleshootingAlreadyExistsException("이미 트러블슈팅이 등록된 기능입니다.");
        }

        Troubleshooting troubleshooting = new Troubleshooting(
            feature, request.problem(), request.analysis(), request.action(), request.result()
        );
        Troubleshooting saved = troubleshootingRepository.save(troubleshooting);
        feature.setTroubleshooting(saved);

        return TroubleshootingResponse.from(saved);
    }

    @Transactional
    public TroubleshootingResponse update(Long id, TroubleshootingRequest request) {
        Troubleshooting troubleshooting = getOrThrow(id);
        troubleshooting.update(request.problem(), request.analysis(), request.action(), request.result());
        return TroubleshootingResponse.from(troubleshooting);
    }

    @Transactional
    public void delete(Long id) {
        troubleshootingRepository.delete(getOrThrow(id));
    }

    private Troubleshooting getOrThrow(Long id) {
        return troubleshootingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("트러블슈팅을 찾을 수 없습니다."));
    }
}
