#RRS#

##Prerequisite##

Before you run this project, install the following tools.

1. [Apache Maven](http://maven.apache.org)
2. [Java 7](http://www.oracle.com)
3. [Spring ToolSuite](http://www.springsource.org)
4. [MongoDB](http://www.mongodb.org)

##Run Project##

1. (Optionally)Import the codes as an *Existing Maven Project* into your IDE workspace. 
2. Start MongoDb, please refer the mongodb manual for more detailed info.
3. Open command prompt, execute **mvn jetty:run** to run the project in an embeded Jetty server. If you are using STS, right click the project node, select *Run as*/*Run configuration*, and create a custom *Maven build* configuration, set the goal value "jetty:run".

**More detailed setup guide is provided in the wiki page**
