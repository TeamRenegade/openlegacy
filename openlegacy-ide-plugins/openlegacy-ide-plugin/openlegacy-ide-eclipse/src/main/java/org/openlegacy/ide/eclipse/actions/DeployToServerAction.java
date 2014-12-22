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

package org.openlegacy.ide.eclipse.actions;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeSelection;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.PopupUtil;

import java.text.MessageFormat;

/**
 * @author Ivan Bort
 * 
 */
public class DeployToServerAction extends AbstractAction {

	@Override
	public void run(IAction action) {
		IProject project = (IProject)((TreeSelection)getSelection()).getFirstElement();
		EclipseDesignTimeExecuter.instance().copyCodeGenerationTemplates(project);

		// check <packaging> value in pom.xml
		if (!EclipseDesignTimeExecuter.instance().isSupportDirectDeployment(project)) {
			PopupUtil.warn(Messages.getString("warn_direct_deployment_not_supported"));
			return;
		}
		// check if war exist and prompt the user to build it if not exist
		String warFileName = project.getName() + ".war";
		IFile warFile = project.getFile("target/" + warFileName);
		if (!warFile.exists()) {
			boolean answer = PopupUtil.question(MessageFormat.format(Messages.getString("question_run_build_war_launch"),
					warFileName));
			if (answer) {
				ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
				ILaunchConfigurationType type = lm.getLaunchConfigurationType("org.eclipse.m2e.Maven2LaunchConfigurationType");
				try {
					ILaunchConfiguration[] configurations = lm.getLaunchConfigurations(type);
					for (ILaunchConfiguration configuration : configurations) {
						if (StringUtils.equals(configuration.getName(), "build-war")) {
							DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
							break;
						}
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			return;
		}

		DeployToServerDialog dialog = new DeployToServerDialog(getShell(), project);
		dialog.open();
	}
}
