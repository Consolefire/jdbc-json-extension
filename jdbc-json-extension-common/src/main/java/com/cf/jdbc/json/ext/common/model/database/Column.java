
package com.cf.jdbc.json.ext.common.model.database;

import com.cf.jdbc.json.ext.common.model.DynamicMaskAware;
import com.cf.jdbc.json.ext.common.model.Mask;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Column extends BaseDbModel implements Serializable, Comparable<Column>,
		DynamicMaskAware {

	@EqualsAndHashCode.Include
	private String tableName;
	@EqualsAndHashCode.Include
	private Integer columnID;
	private Boolean nullable;
	private Integer dataType;
	private String typeName;
	private Integer size;
	private Integer precision;
	private Object defaultValue;
	private Boolean primaryKey;
	private Boolean foreignKey;

	private Mask mask;
	
	@ColumnHeader(title="COLUMN_NAME", index=0)
	public String getName() {
		return super.getName();
	}
	
	@ColumnHeader(title="COLUMN_ID", index=1)
	public Integer getColumnID() {
		return columnID;
	}
	
	@ColumnHeader(title="NULL_ABLE", index=2)
	public Boolean getNullable() {
		if(null == nullable)
			return Boolean.FALSE;
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	@ColumnHeader(title="DATA_TYPE", index=3)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@ColumnHeader(title="COLUMN_SIZE", index=4)
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@ColumnHeader(title="COLUMN_PRECISION", index=5)
	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	@ColumnHeader(title="DEFAULT_VALUE", index=6)
	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@ColumnHeader(title="IS_PRIMARY_KEY", index=7)
	public Boolean getPrimaryKey() {
		if(null == primaryKey)
			return Boolean.FALSE;
		return primaryKey;
	}

	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ColumnHeader(title="IS_FOREIGN_KEY", index=8)
	public Boolean getForeignKey() {
		if(null == foreignKey)
			return Boolean.FALSE;
		return foreignKey;
	}

	public void setForeignKey(Boolean foreignKey) {
		this.foreignKey = foreignKey;
	}

	public void setColumnID(Integer columnID) {
		this.columnID = columnID;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@ColumnHeader(title="COMMENTS", index=9)
	public String getComments() {
		return super.getComments();
	}

	@Override
	public String toString() {
		return getName() + " [ " + typeName + ", (" + size +") " +
			((nullable) ? "NULL" : "NOTNULL") + " ]";
	}

	public int compareTo(Column o) {
		return this.getColumnID().compareTo(o.getColumnID());
	}


	@Override
	public Mask getMask() {
		return this.mask;
	}

	@Override
	public void setMask(Mask mask) {
		this.mask = mask;
	}


}
