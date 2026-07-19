package main.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ICON_COLOR")
public class IconColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icon_color_id")
    private Long icon_color_id;

    @Column(name = "red", nullable = false)
    private Integer red;

    @Column(name = "green", nullable = false)
    private Integer green;

    @Column(name = "blue", nullable = false)
    private Integer blue;

    // No-arg constructor for Hibernate
    public IconColor() {}

    // All-args constructor for record compatibility
    public IconColor(Long icon_color_id, Integer red, Integer green, Integer blue) {
        this.icon_color_id = icon_color_id;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    // Record-like getters
    public Long icon_color_id() { return icon_color_id; }
    public Integer red() { return red; }
    public Integer green() { return green; }
    public Integer blue() { return blue; }

    // Standard getters/setters
    public Long getIcon_color_id() { return icon_color_id; }
    public void setIcon_color_id(Long icon_color_id) { this.icon_color_id = icon_color_id; }

    public Integer getRed() { return red; }
    public void setRed(Integer red) { this.red = red; }

    public Integer getGreen() { return green; }
    public void setGreen(Integer green) { this.green = green; }

    public Integer getBlue() { return blue; }
    public void setBlue(Integer blue) { this.blue = blue; }
}
