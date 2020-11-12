Vaadin 8 IFrame Example
=======================

A simple example of two Vaadin UIs inside each other. Shows how to communicate using JavaScript callback between the frames (instead of server-side communication).


Workflow
========

To compile the entire project, run "mvn install".

To run the application, run "mvn jetty:run" and open http://localhost:8080/ .

To produce a deployable production mode WAR:
- change productionMode to true in the servlet class configuration (nested in the UI class)
- run "mvn clean package"
- test the war file with "mvn jetty:run-war"
