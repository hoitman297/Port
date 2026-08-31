package com.portfolio.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "project_tech_stack",
    uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "tech_stack_id"})
)
public class ProjectTechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_stack_id", nullable = false)
    private TechStack techStack;

    protected ProjectTechStack() {
    }

    public ProjectTechStack(Project project, TechStack techStack) {
        this.project = project;
        this.techStack = techStack;
    }

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public TechStack getTechStack() {
        return techStack;
    }
}
