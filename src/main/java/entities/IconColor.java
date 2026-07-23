package main.java.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ICON_COLOR")
public class IconColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long iconColorId;

    @Column(nullable = false)
    private Integer red;

    @Column(nullable = false)
    private Integer green;

    @Column(nullable = false)
    private Integer blue;

    // No-arg constructor for Hibernate
    public IconColor() {}

    // All-args constructor
    public IconColor(Long iconColorId, Integer red, Integer green, Integer blue) {
        this.iconColorId = iconColorId;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

	public Long getIconColorId() {
		return iconColorId;
	}

	public void setIconColorId(Long iconColorId) {
		this.iconColorId = iconColorId;
	}

	public Integer getRed() {
		return red;
	}

	public void setRed(Integer red) {
		this.red = red;
	}

	public Integer getGreen() {
		return green;
	}

	public void setGreen(Integer green) {
		this.green = green;
	}

	public Integer getBlue() {
		return blue;
	}

	public void setBlue(Integer blue) {
		this.blue = blue;
	}
    
}
