package com.opensymphony.workflow.spi.mongodb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
*
* @author stephen.lane
*/

@Document(collection = "workflow_property_set")
public class PropertySetDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Field("entity_name")
    @Indexed
    private String entityName;
    @Field("entity_id")
    @Indexed
    private String entityId;
    @Indexed
    private String key;
    @Field("item_type")
    @Indexed
    private int itemType;
    @Field("string_value")
    private String stringValue;
    @Field("date_value")
    private Date dateValue;
    @Field("data_value")
    private byte[] dataValue;
    @Field("float_value")
    private double floatValue;
    @Field("int_value")
    private int intValue;
    @Field("long_value")
    private long longValue;
    
   public PropertySetDocument() {
    	
    }

    public PropertySetDocument(String entityName, String entityId, String key) {
        super();
        this.entityName = entityName;
        this.entityId = entityId;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public byte[] getDataValue() {
        return dataValue;
    }

    public void setDataValue(byte[] dataValue) {
        this.dataValue = dataValue;
    }

    public double getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(double floatValue) {
        this.floatValue = floatValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
