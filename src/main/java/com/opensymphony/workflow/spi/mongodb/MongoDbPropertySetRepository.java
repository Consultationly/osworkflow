package com.opensymphony.workflow.spi.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MongoDbPropertySetRepository extends PagingAndSortingRepository<PropertySetDocument, String>, CrudRepository<PropertySetDocument, String> {
    public PropertySetDocument findByEntityNameAndEntityIdAndKey(String entityName, String entityId, String key);
    public List<PropertySetDocument> findAllByEntityNameAndEntityId(String entityName, String entityId);
    public List<PropertySetDocument> findAllByEntityNameAndEntityIdAndItemType(String entityName, String entityId, int itemType);
    @Query("{'entityName' : ?0, entityId' : ?1, 'key': {$regex: ?2 }}")
    public List<PropertySetDocument> findAll(String entityName, String entityId, String key);
    @Query("{'entityName' : ?0, entityId' : ?1, itemType' : ?2, 'key': {$regex: ?3 }}")
    public List<PropertySetDocument> findAll(String entityName, String entityId, int itemType, String key);

    public List<PropertySetDocument> removeByEntityNameAndEntityIdAndKey(String entityName, String entityId, String key);
    public List<PropertySetDocument> removeByEntityNameAndEntityId(String entityName, String entityId);
}

