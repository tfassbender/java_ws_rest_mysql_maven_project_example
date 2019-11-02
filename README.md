# java_ws_rest_mysql_maven_project_example

A simple example project for creating a java webservice that uses rest.  
The service runns on a tomcat docker that is connected to a mysql docker

# Build

The web-archive file can be build using maven: `mvn clean install compile`

The test client (only needed for POST tests) can't be build using maven. The easiest way is building it, using eclipse: Export > Runnable .jar file > ...

# Links

For the complete example with the dockerfiles (but without the sourcecode, but only the compiled code) see: [java_ws_rest_myslq_example](https://github.com/tfassbender/java_ws_rest_mysql_example)
