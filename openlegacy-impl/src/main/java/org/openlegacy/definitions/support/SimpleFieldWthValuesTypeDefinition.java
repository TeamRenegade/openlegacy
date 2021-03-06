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
package org.openlegacy.definitions.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;
import org.openlegacy.definitions.FieldWithValuesTypeDefinition;
import org.openlegacy.terminal.actions.TerminalAction;

import java.io.Serializable;

public class SimpleFieldWthValuesTypeDefinition implements FieldWithValuesTypeDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private RecordsProvider<? extends Session, Object> recordsProvider;
	private Class<?> sourceEntityClass;
	private boolean collectAll;
	private EntityDefinition<?> sourceEntityDefinition;
	private String sourceEntityClassName;

	private String displayFieldName;

	private boolean asWindow;

	private TerminalAction autoSubmitAction;
	private String autoSubmitActionName;

	private String searchField;

	public void setRecordsProvider(RecordsProvider<? extends Session, Object> recordsProvider) {
		this.recordsProvider = recordsProvider;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Session, T> RecordsProvider<S, T> getRecordsProvider() {
		return (RecordsProvider<S, T>)recordsProvider;
	}

	@Override
	public Class<?> getSourceEntityClass() {
		return sourceEntityClass;
	}

	/**
	 * Required for design-time where class doesn't exists yet
	 * 
	 * @param sourceEntityClassName
	 */

	public void setSourceEntityClass(Class<?> sourceEntityClass) {
		this.sourceEntityClass = sourceEntityClass;
	}

	/**
	 * Required for design-time where class doesn't exists yet
	 * 
	 * @return
	 */

	@Override
	public EntityDefinition<?> getSourceEntityDefinition() {
		return sourceEntityDefinition;
	}

	public void setSourceEntityDefinition(EntityDefinition<?> sourceEntityDefinition) {
		this.sourceEntityDefinition = sourceEntityDefinition;
	}

	@Override
	public boolean isCollectAll() {
		return collectAll;
	}

	public void setCollectAllRecords(boolean collectAllRecords) {
		this.collectAll = collectAllRecords;
	}

	@Override
	public String getTypeName() {
		return "fieldWithValues";
	}

	public void setSourceEntityClassName(String sourceEntityClassName) {
		this.sourceEntityClassName = sourceEntityClassName;
	}

	@Override
	public String getSourceEntityClassName() {
		if (sourceEntityClassName != null) {
			return sourceEntityClassName;
		}
		return sourceEntityClass.getSimpleName();
	}

	@Override
	public String getDisplayFieldName() {
		return displayFieldName;
	}

	public void setDisplayFieldName(String displayFieldName) {
		this.displayFieldName = displayFieldName;
	}

	public void setAsWindow(boolean asWindow) {
		this.asWindow = asWindow;
	}

	@Override
	public boolean isAsWindow() {
		return asWindow;
	}

	@Override
	public TerminalAction getAutoSubmitAction() {
		return autoSubmitAction;
	}

	public void setAutoSubmitAction(TerminalAction autoSubmitAction) {
		this.autoSubmitAction = autoSubmitAction;
	}

	@Override
	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	@Override
	public String getAutoSubmitActionName() {
		if (autoSubmitActionName == null && autoSubmitAction != null) {
			autoSubmitActionName = autoSubmitAction.getClass().getSimpleName();
		}
		return autoSubmitActionName;
	}

	public void setAutoSubmitActionName(String autoSubmitActionName) {
		this.autoSubmitActionName = autoSubmitActionName;
	}

}
