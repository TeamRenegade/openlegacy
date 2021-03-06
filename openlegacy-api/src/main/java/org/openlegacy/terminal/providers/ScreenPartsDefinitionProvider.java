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
package org.openlegacy.terminal.providers;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;

import java.util.Collection;

/**
 * Screen parts definitions provider purpose is to return screen parts for a given screen entity.
 * 
 * @author Roi Mor
 */
public interface ScreenPartsDefinitionProvider extends DefinitionsProvider {

	Collection<ScreenPartEntityDefinition> getScreenPartsDefinitions(TerminalSnapshot terminalSnapshot, Class<?> screenEntity);

}
