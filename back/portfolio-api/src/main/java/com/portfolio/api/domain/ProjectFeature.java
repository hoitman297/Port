package com.portfolio.api.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_feature")
public class ProjectFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 200)
    private String name;

    @Lob
    private String description;

    @Lob
    private String reason;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @OneToOne(mappedBy = "feature", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Troubleshooting troubleshooting;

    protected ProjectFeature() {
    }

    public ProjectFeature(Project project, String name, String description, String reason,
                           String imageUrl, Integer sortOrder) {
        this.project = project;
        this.name = name;
        this.description = description;
        this.reason = reason;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public void update(String name, String description, String reason, String imageUrl, Integer sortOrder) {
        this.name = name;
        this.description = description;
        this.reason = reason;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public void setTroubleshooting(Troubleshooting troubleshooting) {
        this.troubleshooting = troubleshooting;
    }

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getReason() {
        return reason;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Troubleshooting getTroubleshooting() {
        return troubleshooting;
    }
}
