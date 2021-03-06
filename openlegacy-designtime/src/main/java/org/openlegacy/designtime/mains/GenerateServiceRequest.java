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
package org.openlegacy.designtime.mains;

import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.enums.BackendSolution;

import java.util.ArrayList;
import java.util.List;

public class GenerateServiceRequest extends AbstractGenerateRequest {

	private String serviceName;
	private UserInteraction userInteraction;
	private List<ServiceParameter> inputParameters = new ArrayList<ServiceParameter>();
	private List<ServiceParameter> outputParameters = new ArrayList<ServiceParameter>();

	private BackendSolution serviceType;

	private boolean generatePool;

	private boolean generateTest = true;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<ServiceParameter> getInputParameters() {
		return inputParameters;
	}

	public List<ServiceParameter> getOutputParameters() {
		return outputParameters;
	}

	public UserInteraction getUserInteraction() {
		return userInteraction;
	}

	public void setUserInteraction(UserInteraction userInteraction) {
		this.userInteraction = userInteraction;
	}

	public BackendSolution getServiceType() {
		return serviceType;
	}

	public void setServiceType(BackendSolution serviceType) {
		this.serviceType = serviceType;
	}

	public boolean isGenerateTest() {
		return generateTest;
	}

	public void setGenerateTest(boolean generateTest) {
		this.generateTest = generateTest;
	}

	public boolean isGeneratePool() {
		return generatePool;
	}

	public void setGeneratePool(boolean generatePool) {
		this.generatePool = generatePool;
	}

}
