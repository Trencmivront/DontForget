package main.java.custom;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DocumentFilterFactory {

	public static DocumentFilter getDocumentFilter(int maxCharacter) {
		return new DocumentFilter() {
//			Here we update the text area same way we update jtable.
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
				if (string == null) {
//					and there is no string to be inserted
					return;
				}
//				Insert the string if it length is smaller than max character length
				if ((fb.getDocument().getLength() + string.length()) <= maxCharacter) {
					super.insertString(fb, offset, string, attr);
				} else {
//					get substring of string if text size is bigger than max character
					int remaining = maxCharacter - fb.getDocument().getLength();
					if (remaining > 0) {
						super.insertString(fb, offset, string.substring(0, remaining), attr);
					}
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (text == null) {
					super.replace(fb, offset, length, null, attrs);
					return;
				}
				int currentLength = fb.getDocument().getLength();
				if ((currentLength - length + text.length()) <= maxCharacter) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					int remaining = maxCharacter - currentLength + length;
					if (remaining > 0) {
						super.replace(fb, offset, length, text.substring(0, remaining), attrs);
					}
				}
			}
		};
	}
	
}
