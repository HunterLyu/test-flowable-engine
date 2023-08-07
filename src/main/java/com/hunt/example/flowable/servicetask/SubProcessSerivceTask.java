package com.hunt.example.flowable.servicetask;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class SubProcessSerivceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("===== SubProcessSerivceTask  =====");
        execution.setVariable("flag", 0);
    }
}
