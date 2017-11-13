package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.api.validation.ValidationResult;

import java.io.Serializable;
import java.util.List;

public class ImportResultStatus implements Serializable {

    private boolean success;
    private List<ValidationResult> validationResults;
    private String errorMessage;
    private String taskId;

    public ImportResultStatus(boolean success, List<ValidationResult> validationResults, String errorMessage, String taskId) {
        this.success = success;
        this.validationResults = validationResults;
        this.errorMessage = errorMessage;
        this.taskId = taskId;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<ValidationResult> getValidationResults() {
        return validationResults;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getTaskId() {
        return taskId;
    }
}
