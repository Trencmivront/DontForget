package main.io.github.trencmivront.dontforget.gui.popups;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.io.github.trencmivront.dontforget.dto.TagDTO;
import main.io.github.trencmivront.dontforget.gui.Main;
import main.io.github.trencmivront.dontforget.gui.panels.TagsPanel;

public class TagsDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(TagsPanel.class.getName());
	private TagsPanel tagsPanel;
	private static TagsDialog tagsDialog;
	
	public void setSelectedTags(List<TagDTO> selectedTags) {
		tagsPanel.setSelectedTags(selectedTags);
	}
	
	public List<TagDTO> getSelectedTags() {
		logger.warn("{}", tagsPanel.getSelectedTags());
		return tagsPanel.getSelectedTags();
	}
	
	public TagsDialog() {
//		only one instance of task window at a time
		if(tagsDialog != null) {
			tagsDialog.dispose();
			tagsDialog = null;
		}
		super(Main.getMain(), "Tags", false);
		tagsDialog = this;
		
		setResizable(false);
		setUndecorated(true);
		setLayout(new BorderLayout(10, 10));
		
		tagsPanel = new TagsPanel();
		
		add(tagsPanel);
		logger.info("Tags dialog initialized");
	}
	
}
