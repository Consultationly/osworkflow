package com.opensymphony.workflow.spi.mongodb;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opensymphony.workflow.spi.Step;

/**
*
* @author stephen.lane
*/

@Document(collection = "workflow_step")
public class StepDocument implements Step {

    @Id
    private String id;
    @Field("due_date")
    private Date dueDate;
    @Field("finish_date")
    private Date finishDate;
    @Field("start_date")
    private Date startDate;
    @DBRef
    private WorkflowEntryDocument entry;
    @Field("previous_steps")
    @DBRef
    private List<StepDocument> previousSteps;
    private String caller;
    private String owner;
    private String status;
    @Field("action_id")
    private int actionId;
    private int stepId;

    public StepDocument() {
    }

    public StepDocument(StepDocument step) {
        this.actionId = step.getActionId();
        this.caller = step.getCaller();
        this.finishDate = step.getFinishDate();
        this.dueDate = step.getDueDate();
        this.startDate = step.getStartDate();

        //do not copy this value, it's for unsaved-value
        //this.id = step.getId();
        this.owner = step.getOwner();
        this.status = step.getStatus();
        this.stepId = step.getStepId();
        this.previousSteps = step.getPreviousSteps();
        this.entry = step.entry;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public int getActionId() {
        return actionId;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCaller() {
        return caller;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setEntry(WorkflowEntryDocument entry) {
        this.entry = entry;
    }

    public WorkflowEntryDocument getEntry() {
        return entry;
    }

    public String getEntryId() {
        return entry.getId();
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public String[] getPreviousStepIds() {
        if (previousSteps == null) {
            return new String[0];
        }

        String[] previousStepIds = new String[previousSteps.size()];
        int i = 0;

        for (Iterator iterator = previousSteps.iterator(); iterator.hasNext();) {
            StepDocument step = (StepDocument) iterator.next();
            previousStepIds[i] = step.getId();
            i++;
        }

        return previousStepIds;
    }

    public void setPreviousSteps(List<StepDocument> previousSteps) {
        this.previousSteps = previousSteps;
    }

    public List<StepDocument> getPreviousSteps() {
        return previousSteps;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public int getStepId() {
        return stepId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof StepDocument)) return false;
        StepDocument o = (StepDocument) obj;
        return o.getId().equals(this.getId());
    }
}

