package apps.position.screens;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableDrilldown;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.List;

@ScreenEntity(validateKeys = false)
@ScreenActions(actions = { @Action(action = TerminalActions.F2.class, displayName = "Save", alias = "save") })
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 23, value = "DISPLAY THE LOADALL SUBFILE") })
public class PositionDrillDownMenu {

	@ScreenField(column = 45, endColumn = 49, row = 23, editable = false)
	String rrn;

	private List<DisplayTheLoadallSubfileRecord> displayTheLoadallSubfileRecords;

	@ScreenTable(startRow = 8, endRow = 12)
	// @ScreenActions(actions = { @Action(action = TerminalActions.F2.class, displayName = "Save", alias = "save") })
	@ScreenTableDrilldown()
	public static class DisplayTheLoadallSubfileRecord {

		@ScreenColumn(startColumn = 12, endColumn = 14, key = true, displayName = "Column1", sampleValue = "123")
		private Integer column1;
		@ScreenColumn(startColumn = 28, endColumn = 39, mainDisplayField = true, displayName = "Column2", sampleValue = "123456789abc", selectionField = true)
		private String column2;
		@ScreenColumn(startColumn = 49, endColumn = 51, displayName = "Column3", sampleValue = "123")
		private Integer column3;

		private String focusField;

		public void focus(String... field) {
			if (field.length > 0) {
				focusField = field[0];
			} else {
				focusField = "column1";
			}
		}

	}

}
