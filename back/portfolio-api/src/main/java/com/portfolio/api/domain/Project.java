package com.portfolio.api.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String summary;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "demo_url")
    private String demoUrl;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectFeature> features = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTechStack> projectTechStacks = new ArrayList<>();

    protected Project() {
    }

    public Project(String title, String summary, String thumbnailUrl, String githubUrl,
                    String demoUrl, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.githubUrl = githubUrl;
        this.demoUrl = demoUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String title, String summary, String thumbnailUrl, String githubUrl,
                        String demoUrl, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.githubUrl = githubUrl;
        this.demoUrl = demoUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<ProjectFeature> getFeatures() {
        return features;
    }

    public List<ProjectTechStack> getProjectTechStacks() {
        return projectTechStacks;
    }
}
