package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaTableModel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.util.Map;
import java.util.Set;

/**
 * @author Ivan Bort
 * 
 */
public class ControlsUpdater {

	public static void updateJpaEntityDetailsControls(JpaEntityModel model, Map<String, Text> mapTexts) {
		if (model == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(DbAnnotationConstants.NAME)) {
				text.setText(model.getName());
			}
		}
	}

	public static void updateJpaTableDetailsControls(JpaTableModel model, Map<String, Text> mapTexts, TableViewer tableViewer) {
		if (model == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(DbAnnotationConstants.NAME)) {
				text.setText(model.getName());
			} else if (key.equals(DbAnnotationConstants.CATALOG)) {
				text.setText(model.getCatalog());
			} else if (key.equals(DbAnnotationConstants.SCHEMA)) {
				text.setText(model.getSchema());
			}
		}
		if (tableViewer != null) {
			tableViewer.setInput(model);
		}
	}

	public static void updateJpaFieldDetailsControls(JpaFieldModel model, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes, Map<String, Label> mapLabels) {
		if (model == null) {
			return;
		}

		// update Text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			Text text = mapTexts.get(key);
			if (key.equals(Constants.FIELD_NAME)) {
				text.setText(model.getFieldName());
			} else if (key.equals(DbAnnotationConstants.NAME)) {
				text.setText(model.getName());
			} else if (key.equals(DbAnnotationConstants.COLUMN_DEFINITION)) {
				text.setText(model.getColumnDefinition());
			} else if (key.equals(DbAnnotationConstants.TABLE)) {
				text.setText(model.getTable());
			} else if (key.equals(DbAnnotationConstants.LENGTH)) {
				text.setText(String.valueOf(model.getLength()));
			} else if (key.equals(DbAnnotationConstants.PRECISION)) {
				text.setText(String.valueOf(model.getPrecision()));
			} else if (key.equals(DbAnnotationConstants.SCALE)) {
				text.setText(String.valueOf(model.getScale()));
			}
		}
		// update CheckBox controls
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			Button button = mapCheckBoxes.get(key);
			if (key.equals(DbAnnotationConstants.UNIQUE)) {
				button.setSelection(model.isUnique());
			} else if (key.equals(DbAnnotationConstants.NULLABLE)) {
				button.setSelection(model.isNullable());
			} else if (key.equals(DbAnnotationConstants.INSERTABLE)) {
				button.setSelection(model.isInsertable());
			} else if (key.equals(DbAnnotationConstants.UPDATABLE)) {
				button.setSelection(model.isUpdatable());
			} else if (key.equals(DbAnnotationConstants.DB_ID_ANNOTATION)) {
				button.setSelection(model.isKey());
			}
		}
		// update Label controls
		mapKeys = mapLabels.keySet();
		for (String key : mapKeys) {
			if (key.equals(Constants.JAVA_TYPE_NAME)) {
				mapLabels.get(key).setText(model.getJavaTypeName());
			}
		}
	}

	public static void updateJpaListFieldDetailsControls(JpaListFieldModel model, Map<String, Text> mapTexts,
			Map<String, Button> mapCheckBoxes, Map<String, CCombo> mapCombos) {
		if (model == null) {
			return;
		}
		// update text controls
		Set<String> mapKeys = mapTexts.keySet();
		for (String key : mapKeys) {
			if (key.equals(DbAnnotationConstants.MAPPED_BY)) {
				mapTexts.get(key).setText(model.getMappedBy());
			} else if (key.equals(DbAnnotationConstants.TARGET_ENTITY)) {
				mapTexts.get(key).setText(model.getTargetEntityClassName());
			} else if (key.equals(Constants.LIST_TYPE_ARG)) {
				mapTexts.get(key).setText(model.getFieldTypeArgs());
			}
		}
		// update check boxes
		mapKeys = mapCheckBoxes.keySet();
		for (String key : mapKeys) {
			if (key.equals(DbAnnotationConstants.ORPHAN_REMOVAL)) {
				mapCheckBoxes.get(key).setSelection(model.isOrphanRemoval());
			}
		}
		// update CCombo controls
		mapKeys = mapCombos.keySet();
		for (String key : mapKeys) {
			if (key.equals(DbAnnotationConstants.CASCADE)) {
				mapCombos.get(key).setText(StringUtils.join(model.getCascade(), ","));
			} else if (key.equals(DbAnnotationConstants.FETCH)) {
				mapCombos.get(key).setText(model.getFetch().toString());
			}
		}
	}
}