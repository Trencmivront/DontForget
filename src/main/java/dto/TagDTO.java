package main.java.dto;

import main.java.entities.Tag;

public class TagDTO {

    private Long tagId;
    private String tagName;
    private Integer iconColorId;

    public TagDTO() {}

    public TagDTO(Long tagId, String tagName, Integer iconColorId) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.iconColorId = iconColorId;
    }

    public TagDTO(Tag tag) {
        this.tagId = tag.getTagId();
        this.tagName = tag.getTagName();
        this.iconColorId = tag.getIconColorId() != null ? tag.getIconColorId().intValue() : null;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getIconColorId() {
        return iconColorId;
    }

    public void setIconColorId(Integer iconColorId) {
        this.iconColorId = iconColorId;
    }
}
