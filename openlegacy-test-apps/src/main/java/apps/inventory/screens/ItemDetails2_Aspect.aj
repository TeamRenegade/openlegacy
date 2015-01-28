// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package apps.inventory.screens;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect ItemDetails2_Aspect {

    declare parents: ItemDetails2 implements ScreenEntity;
    private String ItemDetails2.focusField;
    private List<TerminalActionDefinition> ItemDetails2.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

    

    public String ItemDetails2.getItemNumber(){
    	return this.itemNumber;
    }
    

    public AuditDetails ItemDetails2.getAuditDetails(){
    	return this.auditDetails;
    }
    

    public StockInfo ItemDetails2.getStockInfo(){
    	return this.stockInfo;
    }
    


    public String ItemDetails2.getFocusField(){
    	return focusField;
    }
    public void ItemDetails2.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> ItemDetails2.getActions(){
    	return actions;
    }
    
}
