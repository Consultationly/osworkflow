package com.opensymphony.workflow.spi.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.query.WorkflowExpressionQuery;
import com.opensymphony.workflow.query.WorkflowQuery;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;

public class MongoDbWorkflowStore implements WorkflowStore {

//    @Inject MongoDbWorkflowEntryRepository mongoDbWorkflowEntryRepository;
//    @Inject MongoDbStepRepository mongoDbStepRepository;

    @Override
    public void setEntryState(String entryId, int state) throws StoreException {
        MongoDbWorkflowEntryRepository mongoDbWorkflowEntryRepository = BeanUtil.getBean(MongoDbWorkflowEntryRepository.class);
        WorkflowEntryDocument workflowEntryDocument = mongoDbWorkflowEntryRepository.findById(entryId).orElse(null);
        if (workflowEntryDocument != null) {
            workflowEntryDocument.setState(state);
            mongoDbWorkflowEntryRepository.save(workflowEntryDocument);
        }
    }

    @Override
    public PropertySet getPropertySet(String entryId) throws StoreException {
        HashMap args = new HashMap(1);
        args.put("entityName", "OSWorkflowEntry");
        args.put("entityId", entryId);

        MongoDbPropertySet ps = new MongoDbPropertySet();
        ps.init(Collections.EMPTY_MAP, args);

        return ps;
    }

    @Override
    public Step createCurrentStep(String entryId, int stepId, String owner, Date startDate, Date dueDate, String status, String[] previousIds) throws StoreException {
        MongoDbWorkflowEntryRepository mongoDbWorkflowEntryRepository = BeanUtil.getBean(MongoDbWorkflowEntryRepository.class);
        MongoDbStepRepository mongoDbStepRepository = BeanUtil.getBean(MongoDbStepRepository.class);
        StepDocument step = new StepDocument();
        WorkflowEntryDocument entry = mongoDbWorkflowEntryRepository.findById(entryId).orElse(null);

        step.setEntry(entry);
        step.setStepId(stepId);
        step.setOwner(owner);
        step.setStartDate(startDate);
        step.setDueDate(dueDate);
        step.setStatus(status);

        List<String> stepIdList = new ArrayList(previousIds.length);

        for (int i = 0; i < previousIds.length; i++) {
            stepIdList.add(previousIds[i]);
        }

        if (!stepIdList.isEmpty()) {
            Iterable<StepDocument> currentStepDocuments = mongoDbStepRepository.findAllById(stepIdList);
            List<StepDocument> stepList = new ArrayList<>();
            currentStepDocuments.forEach(stepList::add);
            step.setPreviousSteps(stepList);
        } else {
            step.setPreviousSteps(Collections.EMPTY_LIST);
        }
        mongoDbStepRepository.save(step);

        entry.getCurrentSteps().add(step);
        mongoDbWorkflowEntryRepository.save(entry);

        return step;
    }

    @Override
    public WorkflowEntry createEntry(String workflowName) throws StoreException {
        MongoDbWorkflowEntryRepository mongoDbWorkflowEntryRepository = BeanUtil.getBean(MongoDbWorkflowEntryRepository.class);
        WorkflowEntryDocument workflowEntryDocument = mongoDbWorkflowEntryRepository.save(new WorkflowEntryDocument(workflowName, WorkflowEntry.CREATED));
        return WorkflowEntryDocument.toWorkflowEntry(workflowEntryDocument);
    }

    @Override
    public List findCurrentSteps(String entryId) throws StoreException {
        MongoDbWorkflowEntryRepository mongoDbWorkflowEntryRepository = BeanUtil.getBean(MongoDbWorkflowEntryRepository.class);
        WorkflowEntryDocument entry = mongoDbWorkflowEntryRepository.findById(entryId).orElse(null);
        return entry.getCurrentSteps();
    }

    @Override
    public WorkflowEntry findEntry(String entryId) throws StoreException {
        MongoDbWorkflowEntryRepository mongoDbWorkflowEntryRepository = BeanUtil.getBean(MongoDbWorkflowEntryRepository.class);
        WorkflowEntryDocument entry = mongoDbWorkflowEntryRepository.findById(entryId).orElse(null);
        return WorkflowEntryDocument.toWorkflowEntry(entry);
    }

    @Override
    public List findHistorySteps(String entryId) throws StoreException {
        MongoDbWorkflowEntryRepository mongoDbWorkflowEntryRepository = BeanUtil.getBean(MongoDbWorkflowEntryRepository.class);
        WorkflowEntryDocument entry = mongoDbWorkflowEntryRepository.findById(entryId).orElse(null);
        return entry.getHistorySteps();
    }

    @Override
    public void init(Map props) throws StoreException {
    }

    @Override
    public Step markFinished(Step step, int actionId, Date finishDate, String status, String caller) throws StoreException {
        MongoDbStepRepository mongoDbStepRepository = BeanUtil.getBean(MongoDbStepRepository.class);
        StepDocument currentStep = (StepDocument) step;

        currentStep.setActionId(actionId);
        currentStep.setFinishDate(finishDate);
        currentStep.setStatus(status);
        currentStep.setCaller(caller);

        return mongoDbStepRepository.save(currentStep);
    }

    @Override
    public void moveToHistory(Step step) throws StoreException {
        MongoDbWorkflowEntryRepository mongoDbWorkflowEntryRepository = BeanUtil.getBean(MongoDbWorkflowEntryRepository.class);
        WorkflowEntryDocument entry = mongoDbWorkflowEntryRepository.findById(step.getEntryId()).orElse(null);
        StepDocument historyStep = (StepDocument) step;

        entry.getCurrentSteps().remove(step);
        entry.getHistorySteps().add(historyStep);
        mongoDbWorkflowEntryRepository.save(entry);
    }

    @Override
    public List query(WorkflowQuery query) throws StoreException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List query(WorkflowExpressionQuery query) throws StoreException {
        // TODO Auto-generated method stub
        return null;
    }

}
