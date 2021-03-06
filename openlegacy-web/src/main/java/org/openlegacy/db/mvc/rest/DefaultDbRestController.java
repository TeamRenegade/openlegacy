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
package org.openlegacy.db.mvc.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.db.DbSession;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.db.services.DbService;
import org.openlegacy.db.services.DbService.TableDbObject;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.json.EntitySerializationUtils;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.mvc.AbstractRestController;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.IdClass;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DefaultDbRestController extends AbstractRestController {

	public static final String USER = "user";
	public static final String PASSWORD = "password";

	@Inject
	private DbService dbService;

	@Inject
	private DbSession dbSession;

	@Inject
	private Login dbLoginModule;

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	private Integer pageSize = 20;

	@Override
	public void setRequiresLogin(boolean requiresLogin) {
		this.requiresLogin = requiresLogin;
	}

	private Integer pageNumber = 1;

	private boolean requiresLogin = false;

	private final static Log logger = LogFactory.getLog(DefaultDbRestController.class);

	@RequestMapping(value = "/authenticate", method = RequestMethod.GET, consumes = { JSON, XML })
	public void authenticateUser(HttpServletResponse response) throws IOException {
		authenticate(response);
	}

	@Override
	@RequestMapping(value = "/login", consumes = { JSON, XML })
	public Object login(@RequestParam(USER) String user, @RequestParam(PASSWORD) String password, HttpServletResponse response)
			throws IOException {
		try {
			if (dbLoginModule != null) {
				dbLoginModule.login(user, password);
			} else {
				logger.warn("No login module defined. Skipping login");
			}
		} catch (RegistryException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (LoginException e) {
			sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), response);
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return getMenu();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = { JSON, XML })
	public Object login(@RequestBody String json, HttpServletResponse response) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(json);
		JSONObject jsonObj = (JSONObject)obj;
		try {
			if (dbLoginModule != null) {
				dbLoginModule.login(jsonObj.get("user").toString(), jsonObj.get("password").toString());
			} else {
				logger.warn("No login module defined. Skipping login");
			}
		} catch (RegistryException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (LoginException e) {
			sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), response);
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return getMenu();
	}

	@RequestMapping(value = "/logoff", consumes = { JSON, XML })
	public Object logoff(HttpServletResponse response) throws IOException {
		try {
			if (dbLoginModule != null) {
				dbLoginModule.logoff();
			} else {
				logger.warn("No login module defined. Skipping login");
			}
		} catch (RegistryException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (LoginException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return null;
	}

	@RequestMapping(value = "/{entity}", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getEntity(@PathVariable("entity") String entityName, HttpServletResponse response,
			@RequestParam Map<String, String> allRequestParams) throws IOException {
		try {
			boolean children = true;
			pageNumber = 1;
			if (allRequestParams.containsKey("children")) {
				children = Boolean.parseBoolean(allRequestParams.get("children"));
				allRequestParams.remove("children");
			}

			if (allRequestParams.containsKey("page")) {
				pageNumber = Integer.parseInt(allRequestParams.get("page"));
				allRequestParams.remove("page");
			}
			return getEntityRequest(entityName, allRequestParams, null, children, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	@RequestMapping(value = "/{entity}", method = RequestMethod.POST, consumes = JSON)
	public ModelAndView postEntity(@PathVariable("entity") String entityName,
			@RequestParam(value = ACTION, required = false) String action,
			@RequestParam(value = "children", required = false, defaultValue = "true") boolean children,
			@RequestBody String json, HttpServletResponse response) throws IOException {

		// json = "{\"itemId\":\"111\",\"description\":\"sadasd\", \"notes\":[{\"text\":\"qqqq\"},{\"text\":\"sssss\"}]}";
		return postEntityJson(entityName, action, children, json, response);
	}

	@Override
	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.GET, consumes = { JSON,
			XML })
	protected ModelAndView getEntityWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			@RequestParam(value = "children", required = false, defaultValue = "true") boolean children,
			HttpServletResponse response) throws IOException {
		try {
			return getEntityRequest(entityName, null, key, children, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.DELETE)
	protected void deleteEntityWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			HttpServletResponse response) throws IOException {
		try {
			deleteEntityRequest(entityName, key, response);
		} catch (EntityNotFoundException e) {
			sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage(), response);
		} catch (Exception e) {
			sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), response);
		}
	}

	@Override
	@RequestMapping(value = "/menu", method = RequestMethod.GET, consumes = { JSON, XML })
	public Object getMenu(HttpServletResponse response) throws IOException {
		return super.getMenu(response);
	}

	@Override
	protected ModelAndView getEntityDefinitions(String entityName, HttpServletResponse response) throws IOException {
		EntityDefinition<?> entityDefinitions = dbEntitiesRegistry.get(entityName);
		return new ModelAndView("definitions", "definitions", entityDefinitions);

	}

	protected ModelAndView getEntityRequest(String entityName, Map<String, String> queryConditions, String key, boolean children,
			HttpServletResponse response) throws IOException {
		if (!authenticate(response)) {
			return null;
		}
		Class<?> entityClass = dbEntitiesRegistry.get(entityName).getEntityClass();
		try {
			Object entity = getApiEntity(entityClass, queryConditions, key);
			return getEntityInner(entity, children);
		} catch (EntityNotFoundException e) {
			logger.fatal(e, e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return null;
		}
	}

	protected void deleteEntityRequest(String entityName, String key, HttpServletResponse response)
			throws EntityNotFoundException, Exception {
		Class<?> entityClass = dbEntitiesRegistry.get(entityName).getEntityClass();
		dbService.deleteEntityById(entityClass, toObject(getFirstIdJavaType(entityClass), key.split("\\+")[0]));
	}

	@Override
	protected Object postApiEntity(String entityName, Class<?> entityClass, String key) {
		return getApiEntity(entityClass, null, key);
	}

	protected Object getApiEntity(Class<?> entityClass, Map<String, String> queryConditions, String key) {
		Object[] keys = new Object[0];
		if (key != null) {
			keys = key.split("\\+");

			Object primaryKey = null;
			if (keys.length > 1) {
				try {
					primaryKey = getCompositeKey(entityClass, keys);
				} catch (Exception e) {
					return null;
				}
			} else {
				primaryKey = toObject(getFirstIdJavaType(entityClass), (String)keys[0]);
			}
			return dbService.getEntityById(entityClass, primaryKey);
		} else {
			if (queryConditions != null && queryConditions.size() != 0) {
				return dbService.getEntitiesWithConditions(entityClass, queryConditions);
			} else {
				return dbService.getEntitiesPerPage(entityClass, pageSize, pageNumber);
			}
		}
	}

	private static Object getCompositeKey(Class<?> entityClass, Object[] keys) throws InstantiationException,
			IllegalAccessException {
		IdClass idClass = entityClass.getAnnotation(IdClass.class);
		if (idClass != null) {
			Object compositeClazz = idClass.value().newInstance();
			Field[] fields = compositeClazz.getClass().getDeclaredFields();
			List<Field> idFields = new ArrayList<Field>();
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
					idFields.add(field);
				}
			}
			if (keys.length == idFields.size()) {
				for (int i = 0; i < keys.length; i++) {
					idFields.get(i).setAccessible(true);
					idFields.get(i).set(compositeClazz, toObject(idFields.get(i).getType(), (String)keys[i]));
				}
				return compositeClazz;
			}
		}
		return null;
	}

	private static Class<?> getFirstIdJavaType(Class<?> entityClass) {
		for (Field field : entityClass.getDeclaredFields()) {
			Annotation[] annotations = field.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(javax.persistence.Id.class)) {
					return field.getType();
				}
			}
		}

		return null;
	}

	private static Object toObject(Class<?> clazz, String value) {
		if (Boolean.class == clazz || Boolean.TYPE == clazz) {
			return Boolean.parseBoolean(value);
		}
		if (Byte.class == clazz || Byte.TYPE == clazz) {
			return Byte.parseByte(value);
		}
		if (Short.class == clazz || Short.TYPE == clazz) {
			return Short.parseShort(value);
		}
		if (Integer.class == clazz || Integer.TYPE == clazz) {
			return Integer.parseInt(value);
		}
		if (Long.class == clazz || Long.TYPE == clazz) {
			return Long.parseLong(value);
		}
		if (Float.class == clazz || Float.TYPE == clazz) {
			return Float.parseFloat(value);
		}
		if (Double.class == clazz || Double.TYPE == clazz) {
			return Double.parseDouble(value);
		}
		return value;
	}

	/**
	 * 
	 * @param entity
	 * @param child
	 *            whether to include child entities
	 * @return
	 */
	@Override
	protected ModelAndView getEntityInner(Object entity, boolean children) {
		if (entity == null) {
			throw (new EntityNotFoundException("No entity found"));
		}
		int pageCount = 0;
		if (entity.getClass() == TableDbObject.class) {
			pageCount = ((TableDbObject)entity).getPageCount();
			entity = ProxyUtil.getTargetJpaObject(((TableDbObject)entity).getResult(), children);
		} else {
			entity = ProxyUtil.getTargetJpaObject(entity, children);
		}
		SimpleEntityWrapper wrapper = new SimpleEntityWrapper(entity, null, getActions(entity), pageCount);
		return new ModelAndView(MODEL, MODEL, wrapper);
	}

	@Override
	protected List<ActionDefinition> getActions(Object entity) {
		return new ArrayList<ActionDefinition>();
	}

	/**
	 * Accepts a post request in JSON format, de-serialize it to a entity, and send it to the host
	 * 
	 * @param entityName
	 * @param action
	 * @param json
	 * @param response
	 *            Return HTTP OK (200) is success
	 * @return
	 * @throws IOException
	 */
	@Override
	protected ModelAndView postEntityJson(String entityName, String action, boolean children, String json,
			HttpServletResponse response) throws IOException {
		try {
			return postEntityJsonInner(entityName, null, action, children, json, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	@Override
	protected ModelAndView postEntityJsonWithKey(String entityName, String key, String action, boolean children, String json,
			HttpServletResponse response) throws IOException {
		try {
			return postEntityJsonInner(entityName, key, action, children, json, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	private ModelAndView postEntityJsonInner(String entityName, String key, String action, boolean children, String json,
			HttpServletResponse response) throws IOException {

		Object entity = preSendJsonEntity(entityName, key, json, response);

		Object resultEntity = sendEntity(entity, action);
		return getEntityInner(resultEntity, children);
	}

	@Override
	protected Object preSendJsonEntity(String entityName, String key, String json, HttpServletResponse response)
			throws IOException {
		json = decode(json, "{");

		if (!authenticate(response)) {
			return null;
		}

		Class<?> entityClass = findAndHandleNotFound(entityName, response);
		if (entityClass == null) {
			return null;
		}

		Object entity = null;
		postApiEntity(entityName, entityClass, key);

		try {
			if (json.length() == 0) {
				json = "{}";
			}

			entity = EntitySerializationUtils.deserialize(json, entityClass);
		} catch (Exception e) {
			handleDeserializationException(entityName, response, e);
			return null;
		}
		return entity;
	}

	@Override
	protected ModelAndView postEntityXmlWithKey(String entityName, String key, String action, String xml,
			HttpServletResponse response) throws IOException {
		try {
			return postEntityXmlInner(entityName, key, action, xml, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	@Override
	protected ModelAndView postEntityXml(String entityName, String action, String xml, HttpServletResponse response)
			throws IOException {
		try {
			return postEntityXmlInner(entityName, null, action, xml, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	private ModelAndView postEntityXmlInner(String entityName, String key, String action, String xml, HttpServletResponse response)
			throws IOException {

		xml = decode(xml, "<");

		if (!authenticate(response)) {
			return null;
		}

		Class<?> entityClass = findAndHandleNotFound(entityName, response);
		if (entityClass == null) {
			return null;
		}

		Object entity = null;
		postApiEntity(entityName, entityClass, key);

		try {
			InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
			entity = Unmarshaller.unmarshal(entityClass, inputSource);
		} catch (MarshalException e) {
			handleDeserializationException(entityName, response, e);
			return null;
		} catch (ValidationException e) {
			handleDeserializationException(entityName, response, e);
			return null;
		}

		Object resultEntity = sendEntity(entity, action);
		return getEntityInner(resultEntity, false);
	}

	private static String decode(String content, String encodeIndicator) {
		URI uri = null;
		try {
			if (content.length() > 0 && !content.contains(encodeIndicator)) {
				uri = new URI(content);
				content = uri.getPath();
			}
		} catch (URISyntaxException e) {
			throw (new RuntimeException(e));
		}
		return content;
	}

	/**
	 * Look for the given entity in the registry, and return HTTP 400 (BAD REQUEST) in case it's not found
	 * 
	 * @param entityName
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@Override
	protected Class<?> findAndHandleNotFound(String entityName, HttpServletResponse response) throws IOException {
		Class<?> entityClass = dbEntitiesRegistry.getEntityClass(entityName);
		if (entityClass == null) {
			String message = MessageFormat.format("Entity {0} not found", entityName);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
			logger.error(message);
		}
		return entityClass;
	}

	public static void handleDeserializationException(String entityName, HttpServletResponse response, Exception e)
			throws IOException {
		String message = MessageFormat.format("Unable to desirialize entity {0}", entityName);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
		logger.fatal(message, e);
		return;
	}

	@Override
	@Transactional
	public Object sendEntity(Object entity, String action) {
		Object resultEntity = null;
		if (action == "") {
			resultEntity = dbService.createOrUpdateEntity(entity);

		}
		return resultEntity;
	}

	@Override
	protected boolean authenticate(HttpServletResponse response) throws IOException {
		if (!requiresLogin) {
			return true;
		}

		if (!dbLoginModule.isLoggedIn()) {
			sendError(HttpServletResponse.SC_UNAUTHORIZED, "User unauthorized!", response);
		}

		return true;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	private static void sendError(int errorCode, String message, HttpServletResponse response) throws IOException {
		response.resetBuffer();
		response.setStatus(errorCode);
		response.setHeader("Content-Type", "application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(String.format("{\"error\":\"%s\"}", message));
		response.flushBuffer();
	}

	@Override
	public DbSession getSession() {
		return dbSession;
	}

	private Object getMenu() {
		Menu menuModule = getSession().getModule(Menu.class);
		if (menuModule == null) {
			return null;
		}
		MenuItem menus = menuModule.getMenuTree();
		return menus;
	}

	@Override
	protected EntitiesRegistry<?, ?, ?> getEntitiesRegistry() {
		return dbEntitiesRegistry;
	}

}
