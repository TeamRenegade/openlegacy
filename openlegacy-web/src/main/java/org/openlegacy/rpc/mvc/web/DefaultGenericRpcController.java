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
package org.openlegacy.rpc.mvc.web;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.mvc.MvcUtils;
import org.openlegacy.mvc.web.AbstractGenericEntitiesController;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.utils.RpcEntityUtils;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OpenLegacy default web controller for a rpc session. Handles GET/POST requests of a web application. Works closely with
 * generic.jspx / composite.jspx. Saves the need for a dedicated controller and page for each rpc API, if such doesn't exists.
 * 
 * @author Roi Mor
 * 
 */
@Controller
public class DefaultGenericRpcController extends AbstractGenericEntitiesController<RpcSession> {

	@Inject
	private RpcEntityUtils rpcEntityUtils;

	@RequestMapping(value = "/{entity}/{key:help}")
	public String help(@PathVariable("entity") String entityName, Model uiModel, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		@SuppressWarnings("unchecked")
		Class<RpcEntity> entityClass = (Class<RpcEntity>)findAndHandleNotFound(entityName, response);
		if (entityClass == null) {
			return null;
		}

		RpcEntity rpcEntity = ReflectionUtil.newInstance(entityClass);
		prepareView(rpcEntity, uiModel, false, request);
		return "help";
	}

	@RequestMapping(value = "/{entity}", method = RequestMethod.POST)
	public String postEntity(@PathVariable("entity") String entityName,
			@RequestParam(defaultValue = "", value = ACTION) String action,
			@RequestParam(value = "partial", required = false) String partial, HttpServletRequest request,
			HttpServletResponse response, Model uiModel) throws IOException {

		@SuppressWarnings("unchecked")
		Class<RpcEntity> entityClass = (Class<RpcEntity>)findAndHandleNotFound(entityName, response);
		if (entityClass == null) {
			return null;
		}

		RpcEntity rpcEntity = ReflectionUtil.newInstance(entityClass);

		ServletRequestDataBinder binder = new ServletRequestDataBinder(rpcEntity);
		MvcUtils.registerEditors(binder, getEntitiesRegistry());
		binder.bind(request);

		RpcActionDefinition matchedActionDefinition = rpcEntityUtils.findAction(rpcEntity, action);
		RpcEntity resultEntity = getSession().doAction((RpcAction)matchedActionDefinition.getAction(), rpcEntity);
		if (matchedActionDefinition != null && matchedActionDefinition.getTargetEntity() != null) {
			resultEntity = (RpcEntity)getSession().getEntity(matchedActionDefinition.getTargetEntity());
		}

		return prepareView(resultEntity, uiModel, false, request);
	}

	@Override
	protected ActionDefinition findAction(Object entity, String action) {
		return rpcEntityUtils.findAction((RpcEntity)entity, action);
	}

}
