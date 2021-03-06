// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.terminal.samples.model;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect ItemDetails_Aspect {

    declare parents: ItemDetails implements ScreenEntity;
    private String ItemDetails.focusField;
    private List<TerminalActionDefinition> ItemDetails.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

	

	

	

	

	

    private String ItemDetails.stockGroupDescription;
	

	

	

    

    public Integer ItemDetails.getItemNumber(){
    	return this.itemNumber;
    }
    
    public void ItemDetails.setItemNumber(Integer itemNumber){
    	this.itemNumber = itemNumber;
    }

    public String ItemDetails.getItemDescription(){
    	return this.itemDescription;
    }
    
    public void ItemDetails.setItemDescription(String itemDescription){
    	this.itemDescription = itemDescription;
    }

    public String ItemDetails.getAlphaSearch(){
    	return this.alphaSearch;
    }
    
    public void ItemDetails.setAlphaSearch(String alphaSearch){
    	this.alphaSearch = alphaSearch;
    }

    public String ItemDetails.getSupercedingItemfrom(){
    	return this.supercedingItemfrom;
    }
    
    public void ItemDetails.setSupercedingItemfrom(String supercedingItemfrom){
    	this.supercedingItemfrom = supercedingItemfrom;
    }

    public String ItemDetails.getSupercedingItemto(){
    	return this.supercedingItemto;
    }
    
    public void ItemDetails.setSupercedingItemto(String supercedingItemto){
    	this.supercedingItemto = supercedingItemto;
    }

    public String ItemDetails.getSubstituteItemNumber(){
    	return this.substituteItemNumber;
    }
    
    public void ItemDetails.setSubstituteItemNumber(String substituteItemNumber){
    	this.substituteItemNumber = substituteItemNumber;
    }

    public String ItemDetails.getManufacturersItemNo(){
    	return this.manufacturersItemNo;
    }
    
    public void ItemDetails.setManufacturersItemNo(String manufacturersItemNo){
    	this.manufacturersItemNo = manufacturersItemNo;
    }

    public StockGroup ItemDetails.getStockGroup(){
    	return this.stockGroup;
    }
    
    public void ItemDetails.setStockGroup(StockGroup stockGroup){
    	this.stockGroup = stockGroup;
    }

    public String  ItemDetails.getStockGroupDescription(){
    	return this.stockGroupDescription;
    }
    public Integer ItemDetails.getItemWeight(){
    	return this.itemWeight;
    }
    
    public void ItemDetails.setItemWeight(Integer itemWeight){
    	this.itemWeight = itemWeight;
    }

    public Boolean ItemDetails.getPalletLabelRequired(){
    	return this.palletLabelRequired;
    }
    
    public void ItemDetails.setPalletLabelRequired(Boolean palletLabelRequired){
    	this.palletLabelRequired = palletLabelRequired;
    }

    public ItemDetails2 ItemDetails.getItemDetails2(){
    	return this.itemDetails2;
    }
    


    public String ItemDetails.getFocusField(){
    	return focusField;
    }
    public void ItemDetails.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> ItemDetails.getActions(){
    	return actions;
    }
    
}
