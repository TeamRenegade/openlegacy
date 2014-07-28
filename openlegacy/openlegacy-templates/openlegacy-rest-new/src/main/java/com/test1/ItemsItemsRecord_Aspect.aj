// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.test1;

import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import com.test1.Items.ItemsRecord;

privileged @SuppressWarnings("unused") aspect ItemsRecord_Aspect {
    
    public Integer ItemsRecord.getAction_(){
    	return this.action_;
    }
    
    public void ItemsRecord.setAction_(Integer action_){
    	this.action_ = action_;
    }

    public Integer ItemsRecord.getItemNumber(){
    	return this.itemNumber;
    }
    

    public String ItemsRecord.getItemDescription(){
    	return this.itemDescription;
    }
    

    public String ItemsRecord.getItemName(){
    	return this.itemName;
    }
    


    public int ItemsRecord.hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean ItemsRecord.equals(Object other){
    	// TODO exclude terminal fields
		return EqualsBuilder.reflectionEquals(this,other);
    }
}
