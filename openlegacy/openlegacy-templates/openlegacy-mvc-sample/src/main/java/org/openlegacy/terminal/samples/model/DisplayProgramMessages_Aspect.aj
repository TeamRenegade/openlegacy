// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.terminal.samples.model;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect DisplayProgramMessages_Aspect {

    declare parents: DisplayProgramMessages implements ScreenEntity;
    private String DisplayProgramMessages.focusField;
    private List<TerminalActionDefinition> DisplayProgramMessages.actions = new ArrayList<TerminalActionDefinition>();
    
	

    

    public String DisplayProgramMessages.getMessage(){
    	return this.message;
    }
    


    public String DisplayProgramMessages.getFocusField(){
    	return focusField;
    }
    public void DisplayProgramMessages.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> DisplayProgramMessages.getActions(){
    	return actions;
    }
    
}
