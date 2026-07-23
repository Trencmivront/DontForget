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
