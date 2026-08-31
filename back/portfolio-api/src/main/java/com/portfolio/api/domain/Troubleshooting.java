package com.portfolio.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "troubleshooting")
public class Troubleshooting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false, unique = true)
    private ProjectFeature feature;

    @Lob
    @Column(nullable = false)
    private String problem;

    @Lob
    @Column(nullable = false)
    private String analysis;

    @Lob
    @Column(name = "`action`", nullable = false)
    private String action;

    @Lob
    @Column(nullable = false)
    private String result;

    protected Troubleshooting() {
    }

    public Troubleshooting(ProjectFeature feature, String problem, String analysis, String action, String result) {
        this.feature = feature;
        this.problem = problem;
        this.analysis = analysis;
        this.action = action;
        this.result = result;
    }

    public void update(String problem, String analysis, String action, String result) {
        this.problem = problem;
        this.analysis = analysis;
        this.action = action;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public ProjectFeature getFeature() {
        return feature;
    }

    public String getProblem() {
        return problem;
    }

    public String getAnalysis() {
        return analysis;
    }

    public String getAction() {
        return action;
    }

    public String getResult() {
        return result;
    }
}
