package com.opensymphony.workflow.spi.mongodb;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.opensymphony.workflow.spi.SimpleWorkflowEntry;

public interface MongoDbWorkflowEntryRepository extends PagingAndSortingRepository<WorkflowEntryDocument, String> {

}

