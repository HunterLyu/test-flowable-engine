package com.hunt.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunt.example.service.TestMsgFlowService;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.rest.service.api.RestResponseFactory;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping(path = "/")
public class TestController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    public static final ObjectMapper OBJECT_MAPPER = JacksonUtils.enhancedObjectMapper();
    @Autowired
    private CmmnRuntimeService cmmnRuntimeService;
    @Autowired
    private CmmnTaskService cmmnTaskService;
    @Autowired
    private CmmnHistoryService cmmnHistoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ReviewEventCounter reviewEventCounter;

    @Autowired
    private TestMsgFlowService testMsgFlowService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public TestController() {
    }

    @PostMapping("/process/complete-task")
    public String completeTask2(@RequestParam String processId, @RequestParam String taskKey, @RequestBody Map<String, Object> params){
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        Task task = this.taskService.createTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .taskDefinitionKey(taskKey)
                .singleResult();
        this.taskService.complete(task.getId(), params);
        return "OK";
    }

    @GetMapping(value = "/hello")
    @ResponseBody
    public String startMsgFlow() {
        return "hello";
    }

    @GetMapping(value = "/startMsgFlow")
    @ResponseBody
    public String startMsgFlow(@RequestParam(value = "userId", required = false, defaultValue = "1") String userId,
                               @RequestParam(value = "processKey") String processKey) {

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processKey, param);

        return "提交成功，流程ID为：" + pi.getProcessInstanceId();
    }

    @RequestMapping(value = "/processDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, @RequestParam(value = "instanceId",
            required = false) String instanceId) throws Exception {
        testMsgFlowService.genProcessDiagram(httpServletResponse, instanceId);

    }

    @GetMapping("/dashboard")
    @ResponseBody
    public DashboardData getDashboardData() {

        long caseInstanceCount = cmmnRuntimeService.createCaseInstanceQuery().count();
        long processInstanceCount = runtimeService.createProcessInstanceQuery().count();
        long taskCount = cmmnHistoryService.createHistoricTaskInstanceQuery().count();
        long reviewEventCount = reviewEventCounter.getEventCount();

        return new DashboardData(caseInstanceCount, processInstanceCount, taskCount, reviewEventCount);
    }

    @Autowired
    protected RestResponseFactory restResponseFactory;
    public List<ProcessInstanceResponse>getProcessInstanceRest(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        List<ProcessInstanceResponse> processInstanceResponseList = restResponseFactory.createProcessInstanceResponseList(list);

        return processInstanceResponseList;
    }

    @GetMapping("/deploy")
    @ResponseBody
    public Map deploy(@RequestParam(value = "processId", required = false, defaultValue = "1") String processId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String result = "success";
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addClasspathResource("processes/" + processId + ".bpmn20.xml")
                    .deploy();

            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .singleResult();
            System.out.println("Found process definition : " + processDefinition.getName());
        } catch (Exception e) {
            log.error("deploy error: processId=" + processId, e);
            result = e.getMessage();
        } finally {
            map.put("result", result);
        }
        return map;
    }


    // 发送消息
    @GetMapping("/kafka/send")
    @ResponseBody
    public String sendMessage1(@RequestParam(value = "userId", required = false, defaultValue = "1") String userId
            , @RequestParam(value = "topic") String topic) throws JsonProcessingException, ExecutionException, InterruptedException {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        String kafkaMsg = OBJECT_MAPPER.writeValueAsString(param);

        CompletableFuture<Void> result = kafkaTemplate.send(topic, kafkaMsg)
                .thenAccept(this::onSuccess)
                .exceptionally(this::onFailure);

//        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.executeInTransaction(operations -> {
//            CompletableFuture<SendResult<String, Object>> result = operations
//                    .send(topic, kafkaMsg)
//                    .thenApply(this::onSuccess)
//                    .exceptionally(this::onFailure);
//            return result;
//        });

        return result.toString();
    }


    // 发送消息
//    @GetMapping("/kafka/send")
//    @ResponseBody
//    public String sendMessage1(@RequestParam(value = "userId", required = false, defaultValue = "1") String userId
//            , @RequestParam(value = "topic") String topic) throws Exception {
//        Map<String, Object> param = new HashMap<>();
//        param.put("userId", userId);
//
//        String kafkaMsg = OBJECT_MAPPER.writeValueAsString(param);
//
//        ListenableFuture<SendResult<String, Object>> send = kafkaTemplate.send(topic, kafkaMsg);
//        send.addCallback(new ListenableFutureCallback<SendResult<String,
//                Object>>() {
//            @Override
//            public void onSuccess(SendResult<String, Object> result) {
//                log.info("发送消息成功：" + result.getRecordMetadata().topic() + "-"
//                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                log.error("发送消息失败.", ex);
//            }
//        });
//
//        return send.get().toString();
//    }


    public Void onFailure(Throwable ex) {
        log.error("send kafka message failed：" + ex.getMessage());

        return null;
    }

    public SendResult<String, Object> onSuccess(SendResult<String, Object> result) {
        log.info("send kafka message success：" + result.getRecordMetadata().topic() + "-"
                + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());

        return result;
    }

    public static class DashboardData {

        private long caseInstanceCount;
        private long processInstanceCount;
        private long taskCount;
        private long reviewEventCount;

        public DashboardData(long caseInstanceCount, long processInstanceCount, long taskCount, long reviewEventCount) {
            this.caseInstanceCount = caseInstanceCount;
            this.processInstanceCount = processInstanceCount;
            this.taskCount = taskCount;
            this.reviewEventCount = reviewEventCount;
        }

        public long getCaseInstanceCount() {
            return caseInstanceCount;
        }

        public void setCaseInstanceCount(long caseInstanceCount) {
            this.caseInstanceCount = caseInstanceCount;
        }

        public long getProcessInstanceCount() {
            return processInstanceCount;
        }

        public void setProcessInstanceCount(long processInstanceCount) {
            this.processInstanceCount = processInstanceCount;
        }

        public long getTaskCount() {
            return taskCount;
        }

        public void setTaskCount(long taskCount) {
            this.taskCount = taskCount;
        }

        public long getReviewEventCount() {
            return reviewEventCount;
        }

        public void setReviewEventCount(long reviewEventCount) {
            this.reviewEventCount = reviewEventCount;
        }
    }
}
