package org.openlegacy.db.definitions;

import org.openlegacy.FieldType;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.support.AbstractFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class SimpleDbColumnFieldDefinition extends AbstractFieldDefinition<DbFieldDefinition> implements DbFieldDefinition {

	private static final long serialVersionUID = 1L;

	// @Column annotation attributes
	private String nameAttr = "";
	private boolean unique = false;
	private boolean nullable = true;
	private boolean insertable = true;
	private boolean updatable = true;
	private boolean isKeyAutoGenerated = false;
	private String columnDefinition = "";
	private String table = "";
	private int length = 255;
	private int precision = 0;
	private int scale = 0;

	// @DbColumn annotation attributes
	// private String displayName = "";
	// private boolean password = false;
	// private String sampleValue = "";
	// private String defaultValue = "";
	// private String helpText = "";
	// private boolean rightToLeft = false;
	// private boolean internal = false;
	private boolean mainDisplayField = false;

	private DbOneToManyDefinition oneToManyDefinition = null;
	private String fieldTypeArgs = null;

	private boolean staticField = false;

	private DbManyToOneDefinition manyToOneDefinition = null;
	private DbJoinColumnDefinition joinColumnDefinition = null;

	public SimpleDbColumnFieldDefinition(String name, Class<? extends FieldType> type) {
		super(name, type);
		setDisplayName("");
		setPassword(false);
		setSampleValue("");
		setDefaultValue("");
		setHelpText("");
		setRightToLeft(false);
		setInternal(false);
	}

	public SimpleDbColumnFieldDefinition(String name, String fieldTypeArgs) {
		super(name, null);
		this.fieldTypeArgs = fieldTypeArgs;
		setDisplayName("");
		setPassword(false);
		setSampleValue("");
		setDefaultValue("");
		setHelpText("");
		setRightToLeft(false);
		setInternal(false);
	}

	@Override
	public int compareTo(FieldDefinition o) {
		return 0;
	}

	public String getNameAttr() {
		return nameAttr;
	}

	public void setNameAttr(String nameAttr) {
		this.nameAttr = nameAttr;
	}

	@Override
	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	@Override
	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	@Override
	public String getColumnDefinition() {
		return columnDefinition;
	}

	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	@Override
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@Override
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	@Override
	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	@Override
	public DbOneToManyDefinition getOneToManyDefinition() {
		return oneToManyDefinition;
	}

	public void setOneToManyDefinition(DbOneToManyDefinition OneToManyDefinition) {
		this.oneToManyDefinition = OneToManyDefinition;
	}

	public String getFieldTypeArgs() {
		return fieldTypeArgs;
	}

	@Override
	public String getExpression() {
		return null;
	}

	@Override
	public int getKeyIndex() {
		return 1;
	}

	@Override
	public boolean isMainDisplayField() {
		return mainDisplayField;
	}

	public void setMainDisplayField(boolean mainDisplayField) {
		this.mainDisplayField = mainDisplayField;
	}

	@Override
	public boolean isKeyAutoGenerated() {
		return isKeyAutoGenerated;
	}

	public void setKeyAutoGenerated(boolean isKeyAutoGenerated) {
		this.isKeyAutoGenerated = isKeyAutoGenerated;
	}

	public boolean isStaticField() {
		return staticField;
	}

	public void setStaticField(boolean staticField) {
		this.staticField = staticField;
	}

	@Override
	public DbManyToOneDefinition getManyToOneDefinition() {
		return manyToOneDefinition;
	}

	public void setManyToOneDefinition(DbManyToOneDefinition manyToOneDefinition) {
		this.manyToOneDefinition = manyToOneDefinition;
	}

	@Override
	public DbJoinColumnDefinition getJoinColumnDefinition() {
		return joinColumnDefinition;
	}

	public void setJoinColumnDefinition(DbJoinColumnDefinition joinColumnDefinition) {
		this.joinColumnDefinition = joinColumnDefinition;
	}

}
