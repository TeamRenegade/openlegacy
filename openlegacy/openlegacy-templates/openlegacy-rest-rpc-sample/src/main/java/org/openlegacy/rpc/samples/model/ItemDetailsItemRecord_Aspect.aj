// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.rpc.samples.model;

import java.util.*;
import org.openlegacy.rpc.samples.model.ItemDetails.*;

privileged @SuppressWarnings("unused") aspect ItemRecord_Aspect {
    
    public String ItemRecord.getItemName(){
    	return this.itemName;
    }
    public void ItemRecord.setItemName(String itemName){
    	this.itemName = itemName;
    }
    public String ItemRecord.getDescription(){
    	return this.description;
    }
    public void ItemRecord.setDescription(String description){
    	this.description = description;
    }
    public Integer ItemRecord.getWeight(){
    	return this.weight;
    }
    public void ItemRecord.setWeight(Integer weight){
    	this.weight = weight;
    }
    
}
