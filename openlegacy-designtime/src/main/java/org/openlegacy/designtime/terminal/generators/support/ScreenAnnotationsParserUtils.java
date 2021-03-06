/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.terminal.generators.support;

import static org.openlegacy.designtime.utils.JavaParserUtil.getAnnotationValue;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.annotations.screen.Action.ActionType;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleFieldWthValuesTypeDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Action;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Field;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldAssignDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenNavigationDefinition;
import org.openlegacy.terminal.services.ScreenIdentification;
import org.openlegacy.terminal.services.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.StringConstants;
import org.openlegacy.utils.StringUtil;

import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;

import java.util.ArrayList;
import java.util.List;

public class ScreenAnnotationsParserUtils {

	public static FieldTypeDefinition loadFieldValues(AnnotationExpr annotationExpr) {
		String sourceScreenClassValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.SOURCE_SCREEN_ENTITY);
		String sourceEntityClassName = StringUtil.toClassName(sourceScreenClassValue);
		String collectAll = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.COLLECT_ALL);
		String asWindow = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.AS_WINDOW);
		String autoSubmitActionValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.AUTO_SUBMIT_ACTION);
		String displayFieldNameValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.DISPLAY_FIELD_NAME);
		String searchFieldValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.SEARCH_FIELD);

		SimpleFieldWthValuesTypeDefinition fieldDefinition = new SimpleFieldWthValuesTypeDefinition();
		fieldDefinition.setSourceEntityClassName(sourceEntityClassName);
		if (StringConstants.TRUE.equals(collectAll)) {
			fieldDefinition.setCollectAllRecords(true);
		}
		if (StringConstants.TRUE.equals((asWindow))) {
			fieldDefinition.setAsWindow(true);
		}
		if (!StringUtils.isEmpty(autoSubmitActionValue)) {
			fieldDefinition.setAutoSubmitActionName(StringUtil.toClassName(autoSubmitActionValue));
		}
		fieldDefinition.setDisplayFieldName(displayFieldNameValue);
		fieldDefinition.setSearchField(searchFieldValue);

		return fieldDefinition;
	}

	public static void loadScreenFieldOrColumnAnnotation(AnnotationExpr annotationExpr, Field field) {
		String editableValue = getAnnotationValue(annotationExpr, AnnotationConstants.EDITABLE);
		String rowValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.ROW);
		String columnValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.COLUMN);
		String displayNameValue = getAnnotationValue(annotationExpr, AnnotationConstants.DISPLAY_NAME);
		String startColumnValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.START_COLUMN);
		String endColumnValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.END_COLUMN);
		String labelColumnValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.LABEL_COLUMN);
		String helpTextValue = getAnnotationValue(annotationExpr, AnnotationConstants.HELP_TEXT);
		String selectionFieldValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.SELECTION_FIELD);
		String keyValue = getAnnotationValue(annotationExpr, AnnotationConstants.KEY);
		String mainDisplayFieldValue = getAnnotationValue(annotationExpr, AnnotationConstants.MAIN_DISPLAY_FIELD);
		// @author Ivan Bort refs assembla #112
		String fieldTypeName = getAnnotationValue(annotationExpr, AnnotationConstants.FIELD_TYPE);
		String rectangleValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.RECTANGLE);
		String passwordValue = getAnnotationValue(annotationExpr, AnnotationConstants.PASSWORD);
		String sampleValue = getAnnotationValue(annotationExpr, AnnotationConstants.SAMPLE_VALUE);
		String defaultValue = getAnnotationValue(annotationExpr, AnnotationConstants.DEFAULT_VALUE);
		String rowsOffset = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.ROWS_OFFSET);
		// @author Ivan Bort refs assembla #235
		String endRowValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.END_ROW);
		String rightToLeftValue = getAnnotationValue(annotationExpr, AnnotationConstants.RIGHT_TO_LEFT);
		String attributeValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.ATTRIBUTE);
		String whenValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.WHEN);
		String unlessValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.UNLESS);
		// @author Ivan Bort refs assembla #483
		String keyIndexValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.KEY_INDEX);
		String internalValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.INTERNAL);
		String globalValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.GLOBAL);
		String nullValueValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.NULL_VALUE);
		String colSpanValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.COL_SPAN);
		String sortIndexValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.SORT_INDEX);
		String targetEntityClassNameValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.TARGET_ENTITY);
		// @author Ivan Bort, refs assembla #595
		String tableKeyValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.TABLE_KEY);
		String forceUpdateValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.FORCE_UPDATE);
		String expressionValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.EXPRESSION);
		String enableLookupValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.ENABLE_LOOKUP);

		field.setSampleValue(StringUtil.isEmpty(sampleValue) ? "" : sampleValue);
		field.setDefaultValue(StringUtil.isEmpty(defaultValue) ? "" : defaultValue);
		field.setFieldTypeName(StringUtil.toClassName(fieldTypeName));

		if (StringConstants.TRUE.equals(rectangleValue)) {
			field.setRectangle(true);
		}
		if (StringConstants.TRUE.equals(passwordValue)) {
			field.setPassword(true);
		}

		if (AnnotationConstants.TRUE.equals(editableValue)) {
			field.setEditable(true);
		}
		if (rowValue != null) {
			field.setRow(Integer.valueOf(rowValue));
		}
		if (columnValue != null) {
			field.setColumn(Integer.valueOf(columnValue));
		}
		String displayName = displayNameValue != null ? displayNameValue : StringUtil.toDisplayName(field.getName());
		field.setDisplayName(displayName);

		if (startColumnValue != null) {
			field.setColumn(Integer.valueOf(startColumnValue));
		}
		if (endColumnValue != null) {
			field.setEndColumn(Integer.valueOf(endColumnValue));
		}
		if (labelColumnValue != null) {
			field.setLabelColumn(Integer.valueOf(labelColumnValue));
		}
		if (helpTextValue != null) {
			field.setHelpText(StringUtil.stripQuotes(helpTextValue));
		}

		if (StringConstants.TRUE.equals(selectionFieldValue)) {
			field.setSelectionField(true);
		}
		if (StringConstants.TRUE.equals(keyValue)) {
			field.setKey(true);
		}
		if (StringConstants.TRUE.equals(mainDisplayFieldValue)) {
			field.setMainDisplayField(true);
		}
		if (rowsOffset != null) {
			field.setRowsOffset(Integer.valueOf(rowsOffset));
		}
		if (endRowValue != null) {
			field.setEndRow(Integer.valueOf(endRowValue));
		}
		if (StringConstants.TRUE.equals(rightToLeftValue)) {
			field.setRightToLeft(true);
		}
		if (attributeValue != null) {
			field.setAttributeName(attributeValue.split("\\.")[1]);
		}
		if (whenValue != null) {
			field.setWhen(StringUtil.stripQuotes(whenValue));
		}
		if (unlessValue != null) {
			field.setUnless(StringUtil.stripQuotes(unlessValue));
		}

		field.setKeyIndex(keyIndexValue != null ? Integer.valueOf(keyIndexValue) : -1);

		if (StringConstants.TRUE.equals(internalValue)) {
			field.setInternal(true);
		}
		if (StringConstants.TRUE.equals(globalValue)) {
			field.setGlobal(true);
		}
		if (nullValueValue != null) {
			field.setNullValue(StringUtil.stripQuotes(nullValueValue));
		}
		if (colSpanValue != null) {
			field.setColSpan(Integer.valueOf(colSpanValue));
		}

		field.setSortIndex(sortIndexValue != null ? Integer.valueOf(sortIndexValue) : -1);

		if (targetEntityClassNameValue != null) {
			field.setTargetEntityClassName(StringUtil.toClassName(targetEntityClassNameValue));
		}
		if (StringConstants.TRUE.equals(tableKeyValue)) {
			field.setTableKey(true);
		}
		if (StringConstants.TRUE.equals(forceUpdateValue)) {
			field.setForceUpdate(true);
		}
		if (expressionValue != null) {
			field.setExpression(StringUtil.stripQuotes(expressionValue));
		}
		field.setEnableLookup(StringConstants.TRUE.equals(enableLookupValue));
	}

	public static List<Action> populateScreenActions(AnnotationExpr annotationExpr) {
		List<Action> actions = new ArrayList<Action>();

		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> actionAttributes = ((NormalAnnotationExpr)annotationExpr).getPairs();
			MemberValuePair actionsKeyValue = actionAttributes.get(0);
			ArrayInitializerExpr actionsPairs = (ArrayInitializerExpr)actionsKeyValue.getValue();
			List<Expression> actionsAnnotations = actionsPairs.getValues();
			for (Expression expression : actionsAnnotations) {
				NormalAnnotationExpr singleAction = (NormalAnnotationExpr)expression;
				String actionClassName = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.ACTION);
				String displayName = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.DISPLAY_NAME);
				String actionAlias = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.ALIAS);
				// used by @TableAction only
				String actionValue = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.ACTION_VALUE);
				String targetEntityName = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.TARGET_ENTITY);
				// @author Ivan Bort, refs assembla #235
				String additionalKeyValue = JavaParserUtil.getAnnotationValue(singleAction,
						ScreenAnnotationConstants.ADDITIONAL_KEY);
				// @author Ivan Bort refs assembla #483
				String focusFieldValue = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.FOCUS_FIELD);
				String typeValue = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.TYPE);
				String sleepValue = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.SLEEP);
				String globalValue = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.GLOBAL);
				String keyboardKeyValue = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.KEYBOARD_KEY);

				int row = JavaParserUtil.getAnnotationValueInt(singleAction, ScreenAnnotationConstants.ROW,
						ScreenAnnotationConstants.ROW_DEFAULT_VALUE);
				int column = JavaParserUtil.getAnnotationValueInt(singleAction, ScreenAnnotationConstants.COLUMN,
						ScreenAnnotationConstants.COLUMN_DEFAULT_VALUE);
				int length = JavaParserUtil.getAnnotationValueInt(singleAction, ScreenAnnotationConstants.LENGTH,
						ScreenAnnotationConstants.LENGTH_DEFAULT_VALUE);
				String when = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.WHEN);

				AdditionalKey additionalKey = AdditionalKey.NONE;
				if (additionalKeyValue != null) {
					additionalKey = AdditionalKey.valueOf(additionalKeyValue.split("\\.")[1]);
				}
				ActionType type = ActionType.GENERAL;
				if (typeValue != null) {
					type = ActionType.valueOf(typeValue.split("\\.")[1]);
				}
				int sleep = 0;
				if (sleepValue != null) {
					sleep = Integer.valueOf(sleepValue);
				}
				boolean global = true;
				if (StringConstants.FALSE.equals(globalValue)) {
					global = false;
				}

				Action action = new Action(actionAlias, StringUtil.toClassName(actionClassName), displayName, additionalKey, row,
						column, length, when, focusFieldValue, type, sleep, global);
				action.setActionValue(actionValue == null ? "" : actionValue);
				action.setTargetEntityName(StringUtil.toClassName(targetEntityName));
				action.setKeyboardKeyName(StringUtil.toClassName(keyboardKeyValue));
				actions.add(action);
			}
		}
		return actions;
	}

	public static NavigationDefinition populateNavigation(AnnotationExpr annotationExpr) {
		SimpleScreenNavigationDefinition navigationDefinition = new SimpleScreenNavigationDefinition();

		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> navigationAttributes = ((NormalAnnotationExpr)annotationExpr).getPairs();

			for (MemberValuePair memberValuePair : navigationAttributes) {
				String attributeValue = memberValuePair.getValue().toString();
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.ACCESSED_FROM)) {
					navigationDefinition.setAccessedFromEntityName(StringUtil.stripQuotes(StringUtil.toClassName(attributeValue)));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.REQUIRES_PARAMETERS)) {
					navigationDefinition.setRequiresParameters(Boolean.valueOf(attributeValue));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.TERMINAL_ACTION)) {
					navigationDefinition.setTerminalActionName(StringUtil.toClassName(attributeValue));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.ADDITIONAL_KEY)) {
					navigationDefinition.setAdditionalKey(AdditionalKey.valueOf(attributeValue.split("\\.")[1]));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.EXIT_ACTION)) {
					navigationDefinition.setExitActionName(StringUtil.toClassName(attributeValue));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.EXIT_ADDITIONAL_KEY)) {
					navigationDefinition.setExitAdditionalKey(AdditionalKey.valueOf(attributeValue.split("\\.")[1]));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.ASSIGNED_FIELDS)) {
					navigationDefinition.setAssignedFields(populateAssignedFields(memberValuePair.getValue()));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.DRILLDOWN_VALUE)) {
					navigationDefinition.setDrilldownValue(StringUtil.stripQuotes(attributeValue));
				}
			}
		}
		return navigationDefinition;
	}

	private static List<FieldAssignDefinition> populateAssignedFields(Expression expression) {
		List<FieldAssignDefinition> list = new ArrayList<FieldAssignDefinition>();
		if (expression instanceof ArrayInitializerExpr) {
			List<Expression> values = ((ArrayInitializerExpr)expression).getValues();
			for (Expression expr : values) {
				if (expr instanceof NormalAnnotationExpr
						&& (((NormalAnnotationExpr)expr).getName().getName().equals(AssignedField.class.getSimpleName()))) {
					NormalAnnotationExpr annotationExpr = (NormalAnnotationExpr)expr;
					List<MemberValuePair> pairs = annotationExpr.getPairs();
					String name = null;
					String value = null;
					for (MemberValuePair pair : pairs) {
						String attrValue = pair.getValue().toString();
						if (pair.getName().equals(ScreenAnnotationConstants.FIELD)) {
							name = StringUtil.stripQuotes(attrValue);
						}
						if (pair.getName().equals(ScreenAnnotationConstants.VALUE)) {
							value = StringUtil.stripQuotes(attrValue);
						}
					}
					list.add(new SimpleFieldAssignDefinition(name, value));
				}
			}
		}
		return list;
	}

	public static ScreenIdentification populateScreenIdentification(AnnotationExpr annotationExpr) {
		SimpleScreenIdentification identification = new SimpleScreenIdentification();
		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> identifiersAttributes = ((NormalAnnotationExpr)annotationExpr).getPairs();
			if (identifiersAttributes == null) {
				return identification;
			}
			for (MemberValuePair memberValuePair : identifiersAttributes) {
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.IDENTIFIERS)) {
					List<ScreenIdentifier> list = populateScreenIdentifiers(memberValuePair.getValue());
					for (ScreenIdentifier screenIdentifier : list) {
						identification.addIdentifier(screenIdentifier);
					}
				}
			}
		}
		return identification;
	}

	private static List<ScreenIdentifier> populateScreenIdentifiers(Expression expression) {
		List<ScreenIdentifier> list = new ArrayList<ScreenIdentifier>();
		if (expression instanceof ArrayInitializerExpr) {
			List<Expression> values = ((ArrayInitializerExpr)expression).getValues();
			for (Expression expr : values) {
				if (expr instanceof NormalAnnotationExpr
						&& (((NormalAnnotationExpr)expr).getName().getName().equals(Identifier.class.getSimpleName()))) {
					NormalAnnotationExpr annotationExpr = (NormalAnnotationExpr)expr;
					List<MemberValuePair> pairs = annotationExpr.getPairs();
					int row = 0;
					int column = 0;
					String value = null;
					FieldAttributeType attribute = FieldAttributeType.Value;
					for (MemberValuePair pair : pairs) {
						String attrValue = pair.getValue().toString();
						if (pair.getName().equals(ScreenAnnotationConstants.ROW)) {
							row = Integer.parseInt(attrValue);
						}
						if (pair.getName().equals(ScreenAnnotationConstants.COLUMN)) {
							column = Integer.parseInt(attrValue);
						}
						if (pair.getName().equals(ScreenAnnotationConstants.VALUE)) {
							value = StringUtil.stripQuotes(attrValue);
						}
						if (pair.getName().equals(ScreenAnnotationConstants.ATTRIBUTE)) {
							attribute = FieldAttributeType.valueOf(attrValue.split("\\.")[1]);
						}
					}
					list.add(new SimpleScreenIdentifier(new SimpleTerminalPosition(row, column), value, false, attribute));
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @author Ivan Bort, refs assembla #235
	 */
	public static void loadDescriptionField(AnnotationExpr annotationExpr, Field field) {
		String rowValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.ROW);
		String columnValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.COLUMN);
		String endColumnValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.END_COLUMN);

		field.setDescriptionRow(rowValue != null ? Integer.valueOf(rowValue) : null);
		field.setDescriptionColumn(columnValue != null ? Integer.valueOf(columnValue) : null);
		field.setDescriptionEndColumn(endColumnValue != null ? Integer.valueOf(endColumnValue) : null);
	}
	
	public static void loadDynamicField(AnnotationExpr annotationExpr, Field field) {
		String rowValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.ROW);
		String endRowValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.END_ROW);
		String columnValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.COLUMN);
		String endColumnValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.END_COLUMN);
		String text = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.TEXT);
		String fieldOffset = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.FIELD_OFFSET);

		field.setDynamicText(!StringUtil.isEmpty(text) ? text : "");
		field.setDynamicRow(rowValue != null ? Integer.valueOf(rowValue) : null);
		field.setDynamicColumn(columnValue != null ? Integer.valueOf(columnValue) : null);
		field.setDynamicEndRow(endRowValue != null ? Integer.valueOf(endRowValue) : null);
		field.setDynamicEndColumn(endColumnValue != null ? Integer.valueOf(endColumnValue) : null);
		field.setDynamicFieldOffset(fieldOffset != null ? Integer.valueOf(fieldOffset) : null);
		
	}
}
