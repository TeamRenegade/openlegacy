package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;

import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.definitions.BooleanFieldTypeDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcBooleanFieldModel extends RpcFieldModel {

	// annotation attributes
	private String trueValue = null;
	private String falseValue = null;
	private boolean treatEmptyAsNull = false;

	public RpcBooleanFieldModel(RpcNamedObject parent) {
		super(RpcBooleanField.class.getSimpleName(), parent);
		this.javaTypeName = Messages.getString("type.boolean");//$NON-NLS-1$
	}

	public RpcBooleanFieldModel(UUID uuid, RpcNamedObject parent) {
		super(RpcBooleanField.class.getSimpleName(), parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.boolean");//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.rpc.models.NamedObject#init(org.openlegacy.rpc.definitions.RpcFieldDefinition
	 * )
	 */
	@Override
	public void init(RpcFieldDefinition rpcFieldDefinition) {
		super.init(rpcFieldDefinition);
		if (super.isInitialized() && (rpcFieldDefinition.getFieldTypeDefinition() instanceof BooleanFieldTypeDefinition)) {
			BooleanFieldTypeDefinition definition = (BooleanFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
			this.trueValue = definition.getTrueValue();
			this.falseValue = definition.getFalseValue();
			this.treatEmptyAsNull = definition.isTreatNullAsEmpty();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RpcBooleanFieldModel) || !super.equals(obj)) {
			return false;
		}
		RpcBooleanFieldModel model = (RpcBooleanFieldModel)obj;
		if (this.trueValue == null || this.falseValue == null || model.getTrueValue() == null || model.getFalseValue() == null) {
			return false;
		}
		return (this.trueValue.equals(model.getTrueValue())) && (this.falseValue.equals(model.getFalseValue()))
				&& (this.treatEmptyAsNull == model.isTreatEmptyAsNull());
		
	}

	@Override
	public RpcBooleanFieldModel clone() {
		// when cloning, innerBranchesCount should not be modified in parent
		int count = ((RpcNamedObject)this.parent).getInnerBranchesCount();

		RpcBooleanFieldModel model = new RpcBooleanFieldModel(this.uuid, (RpcNamedObject)this.parent);
		fillModel(model);
		((RpcNamedObject)this.parent).setInnerBranchesCount(count);

		model.setTrueValue(this.trueValue);
		model.setFalseValue(this.falseValue);
		model.setTreatEmptyAsNull(this.treatEmptyAsNull);
		return model;
	}

	public String getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(String trueValue) {
		this.trueValue = trueValue;
	}

	public String getFalseValue() {
		return falseValue;
	}

	public void setFalseValue(String falseValue) {
		this.falseValue = falseValue;
	}

	public boolean isTreatEmptyAsNull() {
		return treatEmptyAsNull;
	}

	public void setTreatEmptyAsNull(boolean treatEmptyAsNull) {
		this.treatEmptyAsNull = treatEmptyAsNull;
	}

}
