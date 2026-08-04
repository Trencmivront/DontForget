package main.io.github.trencmivront.dontforget.dto;

import main.io.github.trencmivront.dontforget.entities.Tag;

public class TagDTO {

    private Long tagId;
    private String tagName;
    private Long iconColorId;

    public TagDTO() {}

    public TagDTO(Long tagId, String tagName, Long iconColorId) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.iconColorId = iconColorId;
    }

    public TagDTO(Tag tag) {
        this.tagId = tag.getTagId();
        this.tagName = tag.getTagName();
        this.iconColorId = tag.getIconColorId() != null ? tag.getIconColorId().longValue() : null;
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

    public Long getIconColorId() {
        return iconColorId;
    }

    public void setIconColorId(Long iconColorId) {
        this.iconColorId = iconColorId;
    }
}
