// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.test1;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect StockDetails_Aspect {

    declare parents: StockDetails implements ScreenEntity;
    private String StockDetails.focusField;
    private List<TerminalActionDefinition> StockDetails.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

	

	

	

	

	

	

    

    public Integer StockDetails.getItemNumber(){
    	return this.itemNumber;
    }
    



    public String StockDetails.getNlStockAccount(){
    	return this.nlStockAccount;
    }
    
    public void StockDetails.setNlStockAccount(String nlStockAccount){
    	this.nlStockAccount = nlStockAccount;
    }



    public String StockDetails.getItemTypeBusinessArea(){
    	return this.itemTypeBusinessArea;
    }
    
    public void StockDetails.setItemTypeBusinessArea(String itemTypeBusinessArea){
    	this.itemTypeBusinessArea = itemTypeBusinessArea;
    }



    public String StockDetails.getStockAnalysisCode(){
    	return this.stockAnalysisCode;
    }
    
    public void StockDetails.setStockAnalysisCode(String stockAnalysisCode){
    	this.stockAnalysisCode = stockAnalysisCode;
    }



    public Integer StockDetails.getStandardUnitCost(){
    	return this.standardUnitCost;
    }
    
    public void StockDetails.setStandardUnitCost(Integer standardUnitCost){
    	this.standardUnitCost = standardUnitCost;
    }



    public String StockDetails.getCreatedDate(){
    	return this.createdDate;
    }
    



    public String StockDetails.getCreatedBy(){
    	return this.createdBy;
    }
    



    public String StockDetails.getAmendedDate(){
    	return this.amendedDate;
    }
    



    public String StockDetails.getAmendedBy(){
    	return this.amendedBy;
    }
    




    public String StockDetails.getFocusField(){
    	return focusField;
    }
    public void StockDetails.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> StockDetails.getActions(){
    	return actions;
    }
    
}
