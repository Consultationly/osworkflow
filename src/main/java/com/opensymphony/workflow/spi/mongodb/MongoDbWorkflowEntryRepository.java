package com.opensymphony.workflow.spi.mongodb;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MongoDbWorkflowEntryRepository extends PagingAndSortingRepository<WorkflowEntryDocument, String>, CrudRepository<WorkflowEntryDocument, String> {

}

