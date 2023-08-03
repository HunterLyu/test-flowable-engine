package com.hunt.example.flowable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Setter
@Getter
public class MyEndEventExecutionListener implements ExecutionListener {
    private Object id;

    @Override
    public void notify(DelegateExecution execution) {
        log.info("============ end event fired: event name: {}, vars: {}", execution.getEventName(), execution.getVariables());
    }
}
