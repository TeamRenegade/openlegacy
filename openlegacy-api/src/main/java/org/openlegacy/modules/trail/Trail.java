/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.SessionModule;

/**
 * Session trail module interface. gives access to a session trail, which can be persisted using {@link TrailWriter}.
 * 
 * @author Roi Mor
 */
public interface Trail extends SessionModule {

	public SessionTrail<? extends Snapshot> getSessionTrail();
}
