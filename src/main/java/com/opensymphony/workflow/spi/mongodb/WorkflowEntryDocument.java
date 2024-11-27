package com.opensymphony.workflow.spi.mongodb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
*
* @author stephen.lane
*/
@Document(collection = "workflow_entry")
public class WorkflowEntryDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private int state;
    private int version;
    @Field("workflow_name")
    private String workflowName;
    @Field("current_steps")
    @DBRef
    private List<StepDocument> currentSteps;
    @DBRef
    private List<StepDocument> historySteps;

    public WorkflowEntryDocument() {
    	
    }

    public WorkflowEntryDocument(String workflowName, int state) {
        this.workflowName = workflowName;
        this.state = state;
    }

    public void setCurrentSteps(List<StepDocument> currentSteps) {
        this.currentSteps = currentSteps;
    }

    public List<StepDocument> getCurrentSteps() {
        if (currentSteps == null) {
            currentSteps = new ArrayList<StepDocument>();
        }
        return currentSteps;
    }

    public void setHistorySteps(List<StepDocument> historySteps) {
        this.historySteps = historySteps;
    }

    public List<StepDocument> getHistorySteps() {
        if (historySteps == null) {
            historySteps = new ArrayList<StepDocument>();
        }
        return historySteps;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isInitialized() {
        return state > 0;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void addCurrentSteps(StepDocument step) {
        step.setEntry(this);
        getCurrentSteps().add(step);
    }

    public void addHistorySteps(StepDocument step) {
        step.setEntry(this);
        getHistorySteps().add(step);
    }

    public void removeCurrentSteps(StepDocument step) {
        getCurrentSteps().remove(step);
    }

    protected void setVersion(int version) {
        this.version = version;
    }

    protected int getVersion() {
        return version;
    }

    public static WorkflowEntry toWorkflowEntry(WorkflowEntryDocument workflowEntryDocument) {
        WorkflowEntry WorkflowEntry = null;
        if (workflowEntryDocument != null) {
            WorkflowEntry = new SimpleWorkflowEntry(workflowEntryDocument.getId(), workflowEntryDocument.getWorkflowName(), workflowEntryDocument.getState());
        }
        return WorkflowEntry;
    }

    public static WorkflowEntryDocument fromWorkflowEntry(WorkflowEntry workflowEntry) {
        WorkflowEntryDocument workflowEntryDocument = null;
        if (workflowEntry != null) {
            workflowEntryDocument = new WorkflowEntryDocument(workflowEntry.getWorkflowName(), workflowEntry.getState());
        }
        return workflowEntryDocument;
    }
}
