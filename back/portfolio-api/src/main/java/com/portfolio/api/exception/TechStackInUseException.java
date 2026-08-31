package com.portfolio.api.exception;

import java.util.List;

public class TechStackInUseException extends RuntimeException {

    private final List<Long> usedByProjectIds;

    public TechStackInUseException(List<Long> usedByProjectIds) {
        super("사용 중인 프로젝트가 있어 삭제할 수 없습니다.");
        this.usedByProjectIds = usedByProjectIds;
    }

    public List<Long> getUsedByProjectIds() {
        return usedByProjectIds;
    }
}
