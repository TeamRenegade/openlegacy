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

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.support.SimpleDynamicFieldTypeDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenPartDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenTableDefinition;
import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Action;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Field;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.exceptions.EntityNotAccessibleException;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.StringUtil;

public class ScreenCodeBasedDefinitionUtils {

	private final static Log logger = LogFactory.getLog(ScreenCodeBasedDefinitionUtils.class);

	public static Map<String, ScreenFieldDefinition> getFieldsFromCodeModel(ScreenPojoCodeModel codeModel, String containerPrefix) {

		// add prefix for field parts
		containerPrefix = !StringUtil.isEmpty(containerPrefix) ? containerPrefix + "." : "";

		Collection<Field> fields = codeModel.getFields();

		Map<String, ScreenFieldDefinition> fieldDefinitions = new TreeMap<String, ScreenFieldDefinition>();
		for (Field javaFieldModel : fields) {
			if (!javaFieldModel.isScreenField()) {
				continue;
			}
			SimpleScreenFieldDefinition fieldDefinition = new SimpleScreenFieldDefinition(containerPrefix
					+ javaFieldModel.getName(), null);
			fieldDefinition.setPosition(new SimpleTerminalPosition(javaFieldModel.getRow(), javaFieldModel.getColumn()));
			fieldDefinition.setEditable(javaFieldModel.isEditable());
			if (javaFieldModel.getLabelColumn() != null) {
				fieldDefinition.setLabelPosition(new SimpleTerminalPosition(javaFieldModel.getRow(),
						javaFieldModel.getLabelColumn()));
			}
			fieldDefinition.setDisplayName(javaFieldModel.getDisplayName());
			if (javaFieldModel.getEndColumn() != null) {
				fieldDefinition.setLength(javaFieldModel.getEndColumn() - javaFieldModel.getColumn() + 1);
			}
			fieldDefinition.setFieldTypeDefinition(javaFieldModel.getFieldTypeDefiniton());
			fieldDefinition.setKey(javaFieldModel.isKey());
			fieldDefinition.setHelpText(javaFieldModel.getHelpText());

			// @author Ivan Bort, refs assembla #112
			fieldDefinition.setFieldTypeName(javaFieldModel.getFieldTypeName());
			fieldDefinition.setRectangle(javaFieldModel.isRectangle());
			fieldDefinition.setPassword(javaFieldModel.isPassword());
			fieldDefinition.setSampleValue(javaFieldModel.getSampleValue());
			fieldDefinition.setDefaultValue(javaFieldModel.getDefaultValue());
			fieldDefinition.setJavaTypeName(javaFieldModel.getType());
			// @author Ivan Bort refs assembla #235
			fieldDefinition.setEndPosition(new SimpleTerminalPosition(
					javaFieldModel.getEndRow() == null ? javaFieldModel.getRow() : javaFieldModel.getEndRow(),
					javaFieldModel.getEndColumn() == null ? javaFieldModel.getColumn() + 1 : javaFieldModel.getEndColumn()));
			fieldDefinition.setHelpText(javaFieldModel.getHelpText());
			fieldDefinition.setRightToLeft(javaFieldModel.isRightToLeft());
			fieldDefinition.setWhenFilter(javaFieldModel.getWhen());
			fieldDefinition.setUnlessFilter(javaFieldModel.getUnless());
			fieldDefinition.setAttribute(javaFieldModel.getAttributeName() != null ? FieldAttributeType.valueOf(javaFieldModel.getAttributeName())
					: FieldAttributeType.Value);

			if ((javaFieldModel.getDescriptionRow() != null) || (javaFieldModel.getDescriptionColumn() != null)
					|| (javaFieldModel.getDescriptionEndColumn() != null)) {
				SimpleScreenFieldDefinition descriptionFieldDefinition = new SimpleScreenFieldDefinition();

				descriptionFieldDefinition.setPosition(new SimpleTerminalPosition(
						javaFieldModel.getDescriptionRow() != null ? javaFieldModel.getDescriptionRow() : 0,
						javaFieldModel.getDescriptionColumn() != null ? javaFieldModel.getDescriptionColumn() : 0));
				descriptionFieldDefinition.setEndPosition(new SimpleTerminalPosition(0,
						javaFieldModel.getDescriptionEndColumn() != null ? javaFieldModel.getDescriptionEndColumn() : 0));
				fieldDefinition.setDescriptionFieldDefinition(descriptionFieldDefinition);
			}
			
			if ((!StringUtil.isEmpty(javaFieldModel.getDynamicText())) || (javaFieldModel.getDynamicRow() != null) || (javaFieldModel.getDynamicColumn() != null)
					|| (javaFieldModel.getDynamicEndRow() != null) || (javaFieldModel.getDynamicEndColumn() != null)
					|| (javaFieldModel.getDynamicFieldOffset() != null)) {

				String text = !StringUtil.isEmpty(javaFieldModel.getDynamicText()) ? javaFieldModel.getDynamicText() : "";				
				Integer row = javaFieldModel.getDynamicRow() != null ? javaFieldModel.getDynamicRow() : 0;
				Integer column = javaFieldModel.getDynamicColumn() != null ? javaFieldModel.getDynamicColumn() : 0;
				Integer endRow = javaFieldModel.getDynamicEndRow() != null ? javaFieldModel.getDynamicEndRow() : 0;
				Integer endColumn = javaFieldModel.getDynamicEndColumn() != null ? javaFieldModel.getDynamicEndColumn() : 0;
				Integer fieldOffset = javaFieldModel.getDynamicFieldOffset() != null ? javaFieldModel.getDynamicFieldOffset() : 0;
				
				SimpleDynamicFieldTypeDefinition dynamicDefinition = new SimpleDynamicFieldTypeDefinition(text,row,column, endRow, endColumn, fieldOffset);
				fieldDefinition.setDynamicFieldDefinition(dynamicDefinition);
			}
			
			
			// @author Ivan Bort refs assembla #483
			fieldDefinition.setKeyIndex(javaFieldModel.getKeyIndex());
			fieldDefinition.setInternal(javaFieldModel.isInternal());
			fieldDefinition.setGlobal(javaFieldModel.isGlobal());
			fieldDefinition.setNullValue(javaFieldModel.getNullValue());
			// @author Ivan Bort, refs assembla #595
			fieldDefinition.setTableKey(javaFieldModel.isTableKey());
			fieldDefinition.setForceUpdate(javaFieldModel.isForceUpdate());

			fieldDefinition.setExpression(javaFieldModel.getExpression());
			fieldDefinition.setEnableLookup(javaFieldModel.isEnableLookup());

			fieldDefinitions.put(javaFieldModel.getName(), fieldDefinition);
		}
		return fieldDefinitions;

	}

	/**
	 * 
	 * @param compilationUnit
	 * @param packageDir
	 *            the package source folder. Used for finding related compilation units. May be null in test mode only
	 * @return
	 */
	public static ScreenEntityDefinition getEntityDefinition(CompilationUnit compilationUnit, File packageDir) {
		List<TypeDeclaration> types = compilationUnit.getTypes();
		CodeBasedScreenEntityDefinition screenDefinition = null;

		TypeDeclaration type = compilationUnit.getTypes().get(0);
		List<BodyDeclaration> members = type.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			// look for inner classes
			if (bodyDeclaration instanceof ClassOrInterfaceDeclaration) {
				types.add((TypeDeclaration)bodyDeclaration);
			}
		}

		for (TypeDeclaration typeDeclaration : types) {
			List<AnnotationExpr> annotations = typeDeclaration.getAnnotations();
			if (annotations == null) {
				continue;
			}
			for (AnnotationExpr annotationExpr : annotations) {
				ScreenPojoCodeModel screenEntityCodeModel = null;
				if (JavaParserUtil.hasAnnotation(annotationExpr, ScreenAnnotationConstants.SCREEN_ENTITY_ANNOTATION)
						|| JavaParserUtil.hasAnnotation(annotationExpr,
								ScreenAnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)) {
					screenEntityCodeModel = new DefaultScreenPojoCodeModel(compilationUnit,
							(ClassOrInterfaceDeclaration)typeDeclaration, typeDeclaration.getName(), null,
							(ClassOrInterfaceDeclaration)type);
					screenDefinition = new CodeBasedScreenEntityDefinition(screenEntityCodeModel, packageDir);
				}
				if (JavaParserUtil.hasAnnotation(annotationExpr, ScreenAnnotationConstants.SCREEN_PART_ANNOTATION)) {
					screenEntityCodeModel = new DefaultScreenPojoCodeModel(compilationUnit,
							(ClassOrInterfaceDeclaration)typeDeclaration, typeDeclaration.getName(), null,
							(ClassOrInterfaceDeclaration)type);
					CodeBasedScreenPartDefinition partDefinition = new CodeBasedScreenPartDefinition(screenEntityCodeModel,
							packageDir);
					screenDefinition.getPartsDefinitions().put(partDefinition.getPartName(), partDefinition);
				}
				if (JavaParserUtil.hasAnnotation(annotationExpr, ScreenAnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
					screenEntityCodeModel = new DefaultScreenPojoCodeModel(compilationUnit,
							(ClassOrInterfaceDeclaration)typeDeclaration, typeDeclaration.getName(), null,
							(ClassOrInterfaceDeclaration)type);
					CodeBasedScreenTableDefinition tableDefinition = new CodeBasedScreenTableDefinition(screenEntityCodeModel);
					// concat "s" for table field name: Item class -> "items" field name
					screenDefinition.getTableDefinitions().put(tableDefinition.getTableEntityName() + "s", tableDefinition);
				}
			}
		}

		return screenDefinition;

	}

	public static List<ActionDefinition> getActionsFromCodeModel(ScreenPojoCodeModel codeModel) {
		List<Action> actions = codeModel.getActions();
		List<ActionDefinition> actionDefinitions = new ArrayList<ActionDefinition>();
		for (Action action : actions) {
			String actionName = StringUtil.toClassName(action.getActionName());
			SimpleTerminalActionDefinition actionDefinition = new SimpleTerminalActionDefinition(actionName,
					action.getDisplayName(), new SimpleTerminalPosition(action.getRow(), action.getColumn()));
			if (action.getAlias() != null) {
				actionDefinition.setAlias(StringUtil.stripQuotes(action.getAlias()));
			}
			actionDefinition.setAdditionalKey(action.getAdditionalKey());
			actionDefinition.setLength(action.getLength());
			actionDefinition.setWhen(action.getWhen());
			if (action.getFocusField() != null) {
				actionDefinition.setFocusField(StringUtil.stripQuotes(action.getFocusField()));
			}
			actionDefinition.setType(action.getType());
			actionDefinition.setTargetEntityName(action.getTargetEntityName());
			actionDefinition.setSleep(action.getSleep());
			actionDefinition.setGlobal(action.isGlobal());
			actionDefinition.setKeyboardKeyName(action.getKeyboardKeyName());
			actionDefinition.setRow(action.getRow());
			actionDefinition.setColumn(action.getColumn());
			actionDefinitions.add(actionDefinition);
		}

		return actionDefinitions;
	}

	public static List<EntityDefinition<?>> getChildScreensDefinitions(ScreenPojoCodeModel codeModel, File packageDir) {
		List<EntityDefinition<?>> childDefinitions = new ArrayList<EntityDefinition<?>>();
		if (packageDir == null) {
			logger.warn(MessageFormat.format("Package directory for {0} is not defined. Unable to determine child screens",
					codeModel.getClassName()));
			return childDefinitions;
		}
		Collection<Field> fields = codeModel.getFields();

		for (Field field : fields) {
			if (!field.isChildScreenEntityField()) {
				continue;
			}
			File childSourceFile = new File(packageDir, field.getType() + ".java");
			if (!childSourceFile.exists()) {
				logger.debug(MessageFormat.format("Source file for field {0} is not defined. Unable to find file {1}",
						field.getName(), childSourceFile.getAbsoluteFile()));
				continue;
			}
			try {
				CompilationUnit compilationUnit = JavaParser.parse(childSourceFile, CharEncoding.UTF_8);
				CodeBasedScreenEntityDefinition childEntityDefinition = (CodeBasedScreenEntityDefinition)getEntityDefinition(
						compilationUnit, packageDir);
				childDefinitions.add(childEntityDefinition);
				if (!childEntityDefinition.isChild()) {
					childEntityDefinition.setChild(true);
					logger.warn(MessageFormat.format("A non child screen {0} is related from {1}. Set as child",
							childEntityDefinition.getEntityClassName(), codeModel.getClassName()));
				}
			} catch (ParseException e) {
				logger.warn("Failed parsing java file:" + e.getMessage());
			} catch (IOException e) {
				throw (new EntityNotAccessibleException(e));
			}

		}
		return childDefinitions;
	}

	public static Set<EntityDefinition<?>> getAllChildScreensDefinitions(ScreenPojoCodeModel codeModel, File packageDir) {
		@SuppressWarnings("unchecked")
		Set<EntityDefinition<?>> childs = new ListOrderedSet();
		childs.addAll(getChildScreensDefinitions(codeModel, packageDir));
		for (EntityDefinition<?> childScreenDefinition : childs) {
			Set<EntityDefinition<?>> childScreensDefinitions = childScreenDefinition.getAllChildEntitiesDefinitions();
			if (childScreensDefinitions.size() > 0) {
				logger.info(MessageFormat.format("Adding child screens to list all child screens. Adding: {0}",
						childScreensDefinitions));
				childs.addAll(childScreensDefinitions);
			}
		}
		return childs;
	}

}
