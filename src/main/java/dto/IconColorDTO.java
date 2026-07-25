package main.java.dto;

import main.java.entities.IconColor;

public class IconColorDTO {

    private Long iconColorId;
    private Integer red;
    private Integer green;
    private Integer blue;

    public IconColorDTO() {}

    public IconColorDTO(Long iconColorId, Integer red, Integer green, Integer blue) {
        this.iconColorId = iconColorId;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public IconColorDTO(IconColor iconColor) {
        this.iconColorId = iconColor.getIconColorId();
        this.red = iconColor.getRed();
        this.green = iconColor.getGreen();
        this.blue = iconColor.getBlue();
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
