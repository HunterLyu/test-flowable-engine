Test flowable demo
============================

Some demo code on how to use the event registry in flowable 6.8.0

Prerequisites
-------------
- A Kafka installation on localhost using the default (insecure!) credentials.
- A Mysql instance or others DB for process data storage,
the tables will be created automatically when you boot the application.
- JDK 17
- Modify the applciation.properties file to match your environment.
- To run it by spring boot, remember to edit your run configuration settings in Intellij, choose the option "modify options", 
then check the option of "add dependencies with 'provided' scope to classpath".


Http URL sample
-------------
- To start a process with process difinition: http://localhost/startMsgFlow?userId=xxx&processKey=xxx, you with receive 
a instance id.
- To trace the process, http://localhost/processDiagram?instanceId=xxx
- A simple dashboard: http://localhost/dashboard
- To send a kafka msg: http://localhost/kafka/send?userId=xxx&topic=xxxx, 
the topic is match to the channel destination field in your process definition. For the existing process, there are two 
topic can drive two included tasks in existing process sample to moving on: task1FinishTopic and task2FinishTopic.



To draw a process pipeline
-------------
- By flowable open source app-ui module, download the flowable code of release version 6.8.0,
the repository is: https://github.com/flowable/flowable-engine.git, checkout the tag(important, not branch) of 6.8.0.
Locate the module flowable-app-ui, modify the applciation.properties file to set up your DB connnection, then run it 
by spring boot, and access to: http://localhost:8080/flowable-ui, login as: admin/test.
- By flowable enterprise, just access to cloud.flowable.com, and sign up a trail accout as 30 days free, then create a 
app, then create the process.
