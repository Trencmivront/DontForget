package main.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROJECT")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long project_id;

    @Column(name = "project_title", nullable = false, unique = true, length = 50)
    private String project_title;

    @Column(name = "description", length = 500)
    private String description;

    @Column
    private Integer listOrder;

    @Column(name = "icon_color_id", nullable = false)
    private Long icon_color_id;

    // No-arg constructor
    public Project() {}

    // All-args constructor
    public Project(Long project_id, String project_title, String description, Integer list_order, Long icon_color_id) {
        this.project_id = project_id;
        this.project_title = project_title;
        this.description = description;
        this.listOrder = list_order;
        this.icon_color_id = icon_color_id;
    }

    // Record-like getters
    public Long project_id() { return project_id; }
    public String project_title() { return project_title; }
    public String description() { return description; }
    public Integer list_order() { return listOrder; }
    public Long icon_color_id() { return icon_color_id; }

    // Standard getters/setters
    public Long getProject_id() { return project_id; }
    public void setProject_id(Long project_id) { this.project_id = project_id; }

    public String getProject_title() { return project_title; }
    public void setProject_title(String project_title) { this.project_title = project_title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getList_order() { return listOrder; }
    public void setList_order(Integer list_order) { this.listOrder = list_order; }

    public Long getIcon_color_id() { return icon_color_id; }
    public void setIcon_color_id(Long icon_color_id) { this.icon_color_id = icon_color_id; }
}
