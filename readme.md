Test flowable message driven demo
=================================

Some demo code on how to use the event registry in flowable 6.8.0

Prerequisites
-------------

- A Kafka installation on localhost using the default (insecure!) credentials.
- A Mysql instance or others DB for process data storage, the tables will be created automatically when you boot the
  application.
- JDK 17
- Modify the applciation.properties file to match your environment.
- To run it by spring boot, remember to edit your run configuration settings in Intellij, choose the option "modify
  options", then check the option of "add dependencies with 'provided' scope to classpath".

Http URL sample
---------------

- To start a process with process difinition: http://localhost/startMsgFlow?userId=xxx&processKey=xxx, you with receivea instance id.
- To trace the process, [http://localhost/processDiagram?instanceId=xxx](http://localhost/processDiagram?instanceId=xxx)
- A simple dashboard: [http://localhost/dashboard](http://localhost/dashboard)
- To send a kafka msg: [http://localhost/kafka/send?userId=xxx&topic=xxxx](http://localhost/kafka/send?userId=xxx&topic=xxxx),
  the topic is match to the channel destination field in your process definition. For the existing process, there are
  two topic can drive two included tasks in existing process sample to moving on: task1FinishTopic and task2FinishTopic.

To draw a process pipeline
--------------------------

- By flowable open source app-ui module, download the flowable code of release version 6.8.0, the repository
  is: [https://github.com/flowable/flowable-engine.git](https://github.com/flowable/flowable-engine.git), checkout the tag(important, not branch) of flowable-6.8.0. Locate the module flowable-app-ui, modify the applciation.properties file to set up your DB connnection, then run it by spring boot, and access to: [http://localhost:8080/flowable-ui](http://localhost:8080/flowable-ui), login as: admin/test.
- By flowable enterprise, just access to cloud.flowable.com, and sign up a trail accout as 30 days free, then create an app, then create the process.

Two way to export the event, channel definition files with open source flowable UI modeler
------------------------------------------------------------------------------------------

The open source flowable modeler UI does not support to export the definition file of channel/event as cloud version
dose, so we need to find a way to get them out with the correct data format, thus we can import them into a different
application and proceed the schema auto deployment, especially if your flowable modeler application and your business
application does not share the same DB. Here is a approach to get them:

1. When you draw the process model base on your customised spring boot application:

- First you need to relying on the flowable rest API support, add this dependency to your application pom file:

  ```xml
  <dependency>
    		<groupId>org.flowable</groupId>
    		<artifactId>flowable-engine</artifactId>
    		<version>${flowable.version}</version>
    	</dependency>
    	<dependency>
    		<groupId>org.flowable</groupId>
    		<artifactId>flowable-spring-boot-starter-ui-modeler</artifactId>
    		<version>${flowable.version}</version>
    	</dependency>
    	<dependency>
    		<groupId>org.flowable</groupId>
    		<artifactId>flowable-spring-boot-starter-ui-idm</artifactId>
    		<version>${flowable.version}</version>
    	</dependency>
  ```
- Add these configuration settings to your application.properties file:

```properties
flowable.rest.app.admin.user-id=admin
flowable.rest.app.admin.password=test
flowable.rest.app.admin.first-name=Rest
flowable.rest.app.admin.last-name=Admin
flowable.common.app.idm-url=http://localhost/idm
```

- Boot the application.
- Access to the [http://localhost/modeler](http://localhost/modeler) to draw your process model.
- In modeler ui, click the Apps tab, create a simple app definition, and import your process model in BPMN models option,
  save the app, go back the app list page, click publish the app, then the event/channel definition will deploy to DB at
  the same time. If your main business application is the same one as current, you just can run the process without files
  exportation.
- Use the flowable rest API to get the channel/event definition content and copy it into a new .event or .channel file:
  For example: access [http://localhost/event-registry-api/event-registry-repository/channel-definitions?key=task1FInishChannel&latest=true](http://localhost/event-registry-api/event-registry-repository/channel-definitions?key=task1FInishChannel&latest=true)
  to get the channel id by the channel key "task1FinishChannel", then access [http://localhost/event-registry-api/event-registry-repository/channel-definitions/625e38bd-2c4d-11ee-9bfa-220318a99f17/model](http://localhost/event-registry-api/event-registry-repository/channel-definitions/625e38bd-2c4d-11ee-9bfa-220318a99f17/model)
  to get the channel content, copy the response into a .channel file. Same operation to get the event definition content
  by different API path.

2. When you draw the process model base on the official flowable open source application which download from github ,
   and you boot the application from flowable-app-ui module: Almost the same operation as above, but the rest-api path has a little difference, for example:
   [http://localhost:8080/flowable-ui/event-registry-api/event-registry-repository/channel-definitions/625e38bd-2c4d-11ee-9bfa-220318a99f17/model](http://localhost:8080/flowable-ui/event-registry-api/event-registry-repository/channel-definitions/625e38bd-2c4d-11ee-9bfa-220318a99f17/model)
   You need to add the path context prefix as "/flowable-ui/".
