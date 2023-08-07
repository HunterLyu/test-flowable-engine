package com.hunt.example.flowable;

import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class CheckVarService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        if (execution.getVariable("emailId") == null) {
            // attach error boundary event with error code = WRONG_EMAIL_DETAILS, to capture it.
            throw new BpmnError("Err100", "given emails address is invalid.");
        }
    }
}
