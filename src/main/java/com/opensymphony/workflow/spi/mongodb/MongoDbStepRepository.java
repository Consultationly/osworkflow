package com.opensymphony.workflow.spi.mongodb;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface MongoDbStepRepository extends PagingAndSortingRepository<StepDocument, String> {
}

