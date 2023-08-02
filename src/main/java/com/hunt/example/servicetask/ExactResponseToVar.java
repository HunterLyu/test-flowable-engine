package com.hunt.example.servicetask;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExactResponseToVar implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        Map<String, Object> vars =  execution.getVariables();
        System.out.println("vars is: " + vars);
        ObjectNode node = (ObjectNode) vars.get("holidayRemainingResponseBody");
        String status = node.get("status").textValue();
        execution.setVariable("holidayRemainingStatus", status);
        System.out.println("SetCommentRating ===========================");
    }
}
