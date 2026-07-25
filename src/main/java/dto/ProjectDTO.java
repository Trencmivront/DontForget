package main.java.dto;

import main.java.entities.Project;

public class ProjectDTO {

    private Long projectId;
    private String projectTitle;
    private String description;
    private Long iconColorId;

    public ProjectDTO() {}

    public ProjectDTO(Long projectId, String projectTitle, String description, Long iconColorId) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.description = description;
        this.iconColorId = iconColorId;
    }

    public ProjectDTO(Project project) {
        this.projectId = project.getProjectId();
        this.projectTitle = project.getProjectTitle();
        this.description = project.getDescription();
        this.iconColorId = project.getIconColorId();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIconColorId() {
        return iconColorId;
    }

    public void setIconColorId(Long iconColorId) {
        this.iconColorId = iconColorId;
    }
}
