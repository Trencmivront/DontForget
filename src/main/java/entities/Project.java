package main.java.entities;

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
    @Column
    private Long projectId;

    @Column(nullable = false, unique = true, length = 50)
    private String projectTitle;

    @Column(length = 500)
    private String description;

    @Column
    private Integer listOrder;

    @Column(nullable = false)
    private Long iconColorId;

    // No-arg constructor
    public Project() {}

    // All-args constructor
    public Project(Long projectId, String projectTitle, String description, Integer listOrder, Long iconColorId) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.description = description;
        this.listOrder = listOrder;
        this.iconColorId = iconColorId;
    }



    // Standard getters/setters
    public Long getProjectId() { return projectId; }
    public void setprojectId(Long projectId) { this.projectId = projectId; }

    public String getProjectTitle() { return projectTitle; }
    public void setprojectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getListOrder() { return listOrder; }
    public void setlistOrder(Integer listOrder) { this.listOrder = listOrder; }

    public Long getIconColorId() { return iconColorId; }
    public void seticonColorId(Long iconColorId) { this.iconColorId = iconColorId; }
}
