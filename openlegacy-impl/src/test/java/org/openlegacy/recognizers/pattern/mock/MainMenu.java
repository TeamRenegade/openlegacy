package org.openlegacy.recognizers.pattern.mock;

import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity(name = "ApplinXDemoEnvironment")
public class MainMenu implements org.openlegacy.terminal.ScreenEntity {

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}
}
