// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).
package org.openlegacy.terminal.samples.mvc.controllers;

import java.util.*;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.web.JsonSerializationUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.WebDataBinder;

import org.openlegacy.terminal.samples.model.WorkWithWarehouseDetails;

privileged @SuppressWarnings("unused") aspect WorkWithWarehouseDetailsController_Aspect {

	@Inject
	private TerminalSession WorkWithWarehouseDetailsController.terminalSession;

	@Inject
	private ScreenEntitiesRegistry WorkWithWarehouseDetailsController.screenEntitiesRegistry;

	// handle page initial display
    @RequestMapping(method = RequestMethod.GET)
    public String WorkWithWarehouseDetailsController.show(Model uiModel) {
    	WorkWithWarehouseDetails workWithWarehouseDetails = terminalSession.getEntity(WorkWithWarehouseDetails.class);
	uiModel.addAttribute("workWithWarehouseDetails", workWithWarehouseDetails);
	// show the resulting page for WorkWithWarehouseDetails
        return "WorkWithWarehouseDetails";
    }

    @RequestMapping(value="/help", method = RequestMethod.GET)
    public @ResponseBody String WorkWithWarehouseDetailsController.systemHelp(HttpServletRequest request) throws IOException {
    	URL resource = request.getSession().getServletContext().getResource("/help/WorkWithWarehouseDetails.html");
    	String result = "";
    	if (resource != null){
    		result = IOUtils.toString(resource.openStream());
    	}
    	return result;
    }

	// handle submit action
    @RequestMapping(method = RequestMethod.POST)
    public String WorkWithWarehouseDetailsController.submit(WorkWithWarehouseDetails workWithWarehouseDetails, Model uiModel, HttpServletRequest httpServletRequest) {
	ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.ENTER(), workWithWarehouseDetails);
	// go to the controller for the resulting screen name
		if (resultScreenEntity != null){
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null){
				workWithWarehouseDetails = terminalSession.getEntity(WorkWithWarehouseDetails.class);
		    	uiModel.addAttribute("workWithWarehouseDetails", workWithWarehouseDetails);
		        return "WorkWithWarehouseDetails";
			}
        	return "redirect:" + screenEntityName;
		}
    	return "redirect:/";
		
    }
    
	// handle help action
    @RequestMapping(params="action=help", method = RequestMethod.POST)
    public String WorkWithWarehouseDetailsController.help(WorkWithWarehouseDetails workWithWarehouseDetails, Model uiModel, HttpServletRequest httpServletRequest) {
	ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.F1(), workWithWarehouseDetails);
	// go to the controller for the resulting screen name
		if (resultScreenEntity != null){
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null){
				workWithWarehouseDetails = terminalSession.getEntity(WorkWithWarehouseDetails.class);
		    	uiModel.addAttribute("workWithWarehouseDetails", workWithWarehouseDetails);
		        return "WorkWithWarehouseDetails";
			}
        	return "redirect:" + screenEntityName;
		}
    	return "redirect:/";
		
    }
	// handle exit action
    @RequestMapping(params="action=exit", method = RequestMethod.POST)
    public String WorkWithWarehouseDetailsController.exit(WorkWithWarehouseDetails workWithWarehouseDetails, Model uiModel, HttpServletRequest httpServletRequest) {
	ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.F3(), workWithWarehouseDetails);
	// go to the controller for the resulting screen name
		if (resultScreenEntity != null){
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null){
				workWithWarehouseDetails = terminalSession.getEntity(WorkWithWarehouseDetails.class);
		    	uiModel.addAttribute("workWithWarehouseDetails", workWithWarehouseDetails);
		        return "WorkWithWarehouseDetails";
			}
        	return "redirect:" + screenEntityName;
		}
    	return "redirect:/";
		
    }
	// handle create action
    @RequestMapping(params="action=create", method = RequestMethod.POST)
    public String WorkWithWarehouseDetailsController.create(WorkWithWarehouseDetails workWithWarehouseDetails, Model uiModel, HttpServletRequest httpServletRequest) {
	ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.F6(), workWithWarehouseDetails);
	// go to the controller for the resulting screen name
		if (resultScreenEntity != null){
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null){
				workWithWarehouseDetails = terminalSession.getEntity(WorkWithWarehouseDetails.class);
		    	uiModel.addAttribute("workWithWarehouseDetails", workWithWarehouseDetails);
		        return "WorkWithWarehouseDetails";
			}
        	return "redirect:" + screenEntityName;
		}
    	return "redirect:/";
		
    }
	// handle cancel action
    @RequestMapping(params="action=cancel", method = RequestMethod.POST)
    public String WorkWithWarehouseDetailsController.cancel(WorkWithWarehouseDetails workWithWarehouseDetails, Model uiModel, HttpServletRequest httpServletRequest) {
	ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.F12(), workWithWarehouseDetails);
	// go to the controller for the resulting screen name
		if (resultScreenEntity != null){
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null){
				workWithWarehouseDetails = terminalSession.getEntity(WorkWithWarehouseDetails.class);
		    	uiModel.addAttribute("workWithWarehouseDetails", workWithWarehouseDetails);
		        return "WorkWithWarehouseDetails";
			}
        	return "redirect:" + screenEntityName;
		}
    	return "redirect:/";
		
    }
	// handle previous action
    @RequestMapping(params="action=previous", method = RequestMethod.POST)
    public String WorkWithWarehouseDetailsController.previous(WorkWithWarehouseDetails workWithWarehouseDetails, Model uiModel, HttpServletRequest httpServletRequest) {
	ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.PAGEUP(), workWithWarehouseDetails);
	// go to the controller for the resulting screen name
		if (resultScreenEntity != null){
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null){
				workWithWarehouseDetails = terminalSession.getEntity(WorkWithWarehouseDetails.class);
		    	uiModel.addAttribute("workWithWarehouseDetails", workWithWarehouseDetails);
		        return "WorkWithWarehouseDetails";
			}
        	return "redirect:" + screenEntityName;
		}
    	return "redirect:/";
		
    }
	// handle next action
    @RequestMapping(params="action=next", method = RequestMethod.POST)
    public String WorkWithWarehouseDetailsController.next(WorkWithWarehouseDetails workWithWarehouseDetails, Model uiModel, HttpServletRequest httpServletRequest) {
	ScreenEntity resultScreenEntity = terminalSession.doAction(TerminalActions.PAGEDOWN(), workWithWarehouseDetails);
	// go to the controller for the resulting screen name
		if (resultScreenEntity != null){
			String screenEntityName = screenEntitiesRegistry.get(resultScreenEntity.getClass()).getEntityClassName();
			if (httpServletRequest.getParameter("partial") != null){
				workWithWarehouseDetails = terminalSession.getEntity(WorkWithWarehouseDetails.class);
		    	uiModel.addAttribute("workWithWarehouseDetails", workWithWarehouseDetails);
		        return "WorkWithWarehouseDetails";
			}
        	return "redirect:" + screenEntityName;
		}
    	return "redirect:/";
		
    }
	
	
	@InitBinder
	public void WorkWithWarehouseDetailsController.initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
}
