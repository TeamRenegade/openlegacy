// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.test1;

import java.util.*;
import com.test1.Items.*;

privileged @SuppressWarnings("unused") aspect InnerRecord_Aspect {
    
    public Integer InnerRecord.getItemNumber(){
    	return this.itemNumber;
    }
    public void InnerRecord.setItemNumber(Integer itemNumber){
    	this.itemNumber = itemNumber;
    }
    public String InnerRecord.getItemName(){
    	return this.itemName;
    }
    public void InnerRecord.setItemName(String itemName){
    	this.itemName = itemName;
    }
    public String InnerRecord.getDescription(){
    	return this.description;
    }
    public void InnerRecord.setDescription(String description){
    	this.description = description;
    }
    
}
