package main.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "TAG")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tag_id;

    @Column(name = "tag_name", nullable = false, unique = true, length = 255)
    private String tag_name;

    @Column(name = "icon_color_id", nullable = false)
    private Long icon_color_id;

    // No-arg constructor
    public Tag() {}

    // All-args constructor
    public Tag(Long tag_id, String tag_name, Long icon_color_id) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.icon_color_id = icon_color_id;
    }

    // Record-like getters
    public Long tag_id() { return tag_id; }
    public String tag_name() { return tag_name; }
    public Long icon_color_id() { return icon_color_id; }

    // Standard getters/setters
    public Long getTag_id() { return tag_id; }
    public void setTag_id(Long tag_id) { this.tag_id = tag_id; }

    public String getTag_name() { return tag_name; }
    public void setTag_name(String tag_name) { this.tag_name = tag_name; }

    public Long getIcon_color_id() { return icon_color_id; }
    public void setIcon_color_id(Long icon_color_id) { this.icon_color_id = icon_color_id; }
}
