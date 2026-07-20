package main.java.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "TAG")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long tagId;

    @Column(nullable = false, unique = true, length = 255)
    private String tagName;

    @Column(nullable = false)
    private Long iconColorId;

    // No-arg constructor
    public Tag() {}

    // All-args constructor
    public Tag(Long tagId, String tagName, Long iconColorId) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.iconColorId = iconColorId;
    }

    // Record-like getters
    public Long tagId() { return tagId; }
    public String tagName() { return tagName; }
    public Long iconColorId() { return iconColorId; }

    // Standard getters/setters
    public Long gettagId() { return tagId; }
    public void settagId(Long tagId) { this.tagId = tagId; }

    public String gettagName() { return tagName; }
    public void settagName(String tagName) { this.tagName = tagName; }

    public Long geticonColorId() { return iconColorId; }
    public void seticonColorId(Long iconColorId) { this.iconColorId = iconColorId; }
}
