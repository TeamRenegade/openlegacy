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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;

public class ChildScreenFact implements ScreenFact {

	private ScreenEntityDesigntimeDefinition childScreenEntityDefinition;

	public ChildScreenFact(ScreenEntityDesigntimeDefinition childScreenEntityDefinition) {
		this.childScreenEntityDefinition = childScreenEntityDefinition;
	}

	public ScreenEntityDesigntimeDefinition getChildScreenEntityDefinition() {
		return childScreenEntityDefinition;
	}
}
