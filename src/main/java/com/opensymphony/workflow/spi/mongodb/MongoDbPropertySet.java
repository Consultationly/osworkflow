package com.opensymphony.workflow.spi.mongodb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.opensymphony.module.propertyset.AbstractPropertySet;
import com.opensymphony.module.propertyset.InvalidPropertyTypeException;
import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.util.Data;

@Component
@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE)
public class MongoDbPropertySet extends AbstractPropertySet {

    private static final Log log = LogFactory.getLog(MongoDbPropertySet.class);

//    @Inject MongoDbPropertySetRepository mongoDbPropertySetRepository;

    private String entityId;
    private String entityName;

    @Override
    public boolean exists(String key) throws PropertyException {
        return getType(key) != 0;
    }

    @Override
    protected Object get(int type, String key) throws PropertyException {
        Object o = null;

        PropertySetDocument propertySet = this.findByKey(key);
        if (propertySet != null) {
            int propertyType = propertySet.getItemType();

            if (propertyType != type) {
                throw new InvalidPropertyTypeException();
            }

            switch (type) {
            case BOOLEAN:
                int boolVal = propertySet.getIntValue();
                o = new Boolean(boolVal == 1);

                break;
            case DATA:
                o = propertySet.getDataValue();

                break;
            case DATE:
                o = propertySet.getDateValue();

                break;
            case OBJECT:
                InputStream bis = new ByteArrayInputStream(propertySet.getDataValue());
                try
                {
                    ObjectInputStream is = new ObjectInputStream(bis);
                    o = is.readObject();
                } catch (IOException e) {
                    throw new PropertyException("Error de-serializing object for key '" + key + "' from store:" + e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            case DOUBLE:
                o = new Double(propertySet.getFloatValue());

                break;
            case INT:
                o = new Integer(propertySet.getIntValue());

                break;
            case LONG:
                o = new Long(propertySet.getLongValue());

                break;
            case STRING:
                o = propertySet.getStringValue();

                break;
            case TEXT:
                o = propertySet.getStringValue();

                break;
            case XML:
            default:
                throw new InvalidPropertyTypeException("MongoDbPropertySet doesn't support this type yet.");
            }
        }
        return o;
    }

    @Override
    public Collection getKeys(String prefix, int type) throws PropertyException {
        MongoDbPropertySetRepository mongoDbPropertySetRepository = BeanUtil.getBean(MongoDbPropertySetRepository.class);
        List<PropertySetDocument> propertySetDocuments = null;
        List<String> list = new ArrayList();

        if ((prefix != null) && (type > 0)) {
            propertySetDocuments = mongoDbPropertySetRepository.findAll(this.entityName, this.entityId, type, prefix);
        } else if (prefix != null) {
            propertySetDocuments = mongoDbPropertySetRepository.findAll(this.entityName, this.entityId, prefix);
        } else if (type > 0) {
            propertySetDocuments = mongoDbPropertySetRepository.findAllByEntityNameAndEntityIdAndItemType(this.entityName, this.entityId, type);
        } else {
            propertySetDocuments = mongoDbPropertySetRepository.findAllByEntityNameAndEntityId(this.entityName, this.entityId);
        }

        if (propertySetDocuments != null) {
            for (PropertySetDocument propertySetDocument : propertySetDocuments) {
                list.add(propertySetDocument.getKey());
            }
        }
        return list;
    }

    @Override
    public int getType(String key) throws PropertyException {
        PropertySetDocument propertySet = this.findByKey(key);
        if (propertySet != null) {
            return propertySet.getItemType();
        }
        return 0;
    }

    public void init(Map config, Map args) {
        super.init(config, args);
        this.entityId = (String) args.get("entityId");
        this.entityName = (String) args.get("entityName");
    }

    public void remove() throws PropertyException {
        MongoDbPropertySetRepository mongoDbPropertySetRepository = BeanUtil.getBean(MongoDbPropertySetRepository.class);
        mongoDbPropertySetRepository.removeByEntityNameAndEntityId(this.entityName, this.entityId);
    }

    @Override
    public void remove(String key) throws PropertyException {
        MongoDbPropertySetRepository mongoDbPropertySetRepository = BeanUtil.getBean(MongoDbPropertySetRepository.class);
        mongoDbPropertySetRepository.removeByEntityNameAndEntityIdAndKey(this.entityName, this.entityId, key);
    }

    @Override
    protected void setImpl(int type, String key, Object value) throws PropertyException {
        MongoDbPropertySetRepository mongoDbPropertySetRepository = BeanUtil.getBean(MongoDbPropertySetRepository.class);
        PropertySetDocument propertySet = this.findByKey(key);

        if (propertySet == null) {
            propertySet = new PropertySetDocument(this.entityName, this.entityId, key);
        } else if (propertySet.getItemType() != type) {
            throw new PropertyException("Existing key '" + key + "' does not have matching type of " + type);
        }

        switch (type) {
        case BOOLEAN:
            Boolean boolVal = (Boolean) value;
            propertySet.setIntValue(boolVal.booleanValue() ? 1 : 0);

            break;
        case DATA:
            if ((value instanceof Data)) {
                Data data = (Data) value;
                propertySet.setDataValue(data.getBytes());
            }

            if ((value instanceof byte[]))
            propertySet.setDataValue((byte[]) value);
            break;
        case OBJECT:
            if (!(value instanceof Serializable)) {
                throw new PropertyException(value.getClass() + " does not implement java.io.Serializable");
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try
            {
                ObjectOutputStream os = new ObjectOutputStream(bos);
                os.writeObject(value);
                propertySet.setDataValue(bos.toByteArray());
            } catch (IOException e) {
                throw new PropertyException("I/O Error when serializing object:" + e);
            }

        case DATE:
            Date date = (Date) value;
            propertySet.setDateValue(date);

            break;
        case DOUBLE:
            Double d = (Double) value;
            propertySet.setFloatValue(d.doubleValue());

            break;
        case INT:
            Integer i = (Integer) value;
            propertySet.setIntValue(i);
            break;
        case LONG:
            Long l = (Long) value;
            propertySet.setLongValue(l);

            break;
        case STRING:
            propertySet.setStringValue((String) value);

            break;
        case TEXT:
            propertySet.setStringValue((String) value);

            break;
        case XML:
        default:
            throw new PropertyException("This type isn't supported!");
        }
        mongoDbPropertySetRepository.save(propertySet);
    }

    private PropertySetDocument findByKey(String key) throws PropertyException {
        MongoDbPropertySetRepository mongoDbPropertySetRepository = BeanUtil.getBean(MongoDbPropertySetRepository.class);
        return mongoDbPropertySetRepository.findByEntityNameAndEntityIdAndKey(this.entityName, this.entityId, key);
    }

}
