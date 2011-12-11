package org.openlegacy.designtime.terminal.analyzer;

import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.terminal.model.TableColumn;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.List;

public interface ScreenEntityDefinitionsBuilder {

	void addIdentifiers(SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields);

	/**
	 * Select the best match field for screen name from the list of fields which matches the title pattern
	 */
	void selectPotentialScreenEntityName(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields);

	void addField(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField editableField, TerminalField labelField);

	TableColumn addTableColumn(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields);

	void addTableDefinition(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TableColumn> tableColumns);

	void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, String text, TerminalPosition position, String regex);

	void setSnapshotBorders(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField topBorderField,
			TerminalField buttomBorderField);
}
