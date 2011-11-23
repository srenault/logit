# Logit 0.1

Logit is a application that make easy to analyse logs of all yours projects !
There is a simple web application and a logback appender.
There is nothing to change in your code to send logs to the server.

## Setting up the web app

The web app exposes to clients some REST web services to access to logs' projects :

    GET  /projects/all         : List all logged projects.
    POST /projects/{name}/logs : Add a log entry.
    GET  /projects/{name}/logs : List all logs of the specified project.

A Mongodb database is used to store all the logs.

To configure the connection to the database, edit the xml file 'application.conf' :

    mongo.host=127.0.0.1
    mongo.port=27017
    mongo.db=logIt
    #mongo.username=
    #mongo.password=

Comment the username & password if you don't have.

## Setting up the Logit appender

The logback appender sends logs to the web app.
It is recommended to have a second appender who save locally the logs.
Networks issues can happen !

The logit appender has some dependencies :
 - logback 1.0.0
 - async-http-client-1.7.0

Here is an example of a logit appender configuration :

    <configuration>
     <appender name="LOGIT" class="com.zenexity.logit.appender.LogbackAppender">
         <server>localhost:9000</server>
         <project>ProjectName</project>
         <layout>
           <pattern>{"date" : "%date", "level" :  "%level", "logger" : "%logger", "thread" : "%thread", "message" : "%n%message%n%xException%n"}</pattern>
         </layout>
      </appender>
      <root level="debug">
        <appender-ref ref="LOGIT"/>
      </root>
    </configuration>