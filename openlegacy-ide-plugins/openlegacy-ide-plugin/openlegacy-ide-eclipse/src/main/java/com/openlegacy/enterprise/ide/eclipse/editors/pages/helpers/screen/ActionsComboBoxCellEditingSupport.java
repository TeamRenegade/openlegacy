package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.openlegacy.annotations.screen.Action.ActionType;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

import java.util.List;

public class ActionsComboBoxCellEditingSupport extends EditingSupport {

	private TableViewer viewer;
	private String fieldName;
	private List<String> items;

	public ActionsComboBoxCellEditingSupport(TableViewer viewer, String fieldName, List<String> items) {
		super(viewer);
		this.viewer = viewer;
		this.fieldName = fieldName;
		this.items = items;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new ComboBoxCellEditor(this.viewer.getTable(), this.items.toArray(new String[] {}));
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (this.fieldName.equals(ScreenAnnotationConstants.ADDITIONAL_KEY)) {
			return this.items.indexOf(((ActionModel)element).getAdditionalKey().toString());
		} else if (this.fieldName.equals(ScreenAnnotationConstants.TYPE)) {
			return this.items.indexOf(((ActionModel)element).getType().toString());
		} else if (this.fieldName.equals(ScreenAnnotationConstants.GLOBAL)) {
			return this.items.indexOf(String.valueOf(((ActionModel)element).isGlobal()));
		}
		return 0;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (this.fieldName.equals(ScreenAnnotationConstants.ADDITIONAL_KEY) && ((Integer)value >= 0)) {
			((ActionModel)element).setAdditionalKey(AdditionalKey.valueOf(this.items.get((Integer)value)));
		} else if (this.fieldName.equals(ScreenAnnotationConstants.TYPE) && ((Integer)value >= 0)) {
			((ActionModel)element).setType(ActionType.valueOf(this.items.get((Integer)value)));
		} else if (this.fieldName.equals(ScreenAnnotationConstants.ADDITIONAL_KEY) && ((Integer)value == -1)) {
			((ActionModel)element).setAdditionalKeyDefaultValue();
		} else if (this.fieldName.equals(ScreenAnnotationConstants.TYPE) && ((Integer)value == -1)) {
			((ActionModel)element).setTypeDefaultValue();
		} else if (this.fieldName.equals(ScreenAnnotationConstants.GLOBAL)) {
			((ActionModel)element).setGlobal(Boolean.valueOf(this.items.get((Integer)value)));
		}
		this.viewer.update(element, null);
	}

}
