# ESE Base Project

### Test on Jetty Server

#### Requirements

1. Java JDK8
2. MySQL. Create a root user, who has no password
3. Maven 3.3.9 or later

#### Command line

- download the project from https://github.com/scg-unibe-ch/ese2016-team3 and unzip it
- start commandline from the location where pom.xml is located
- run "mvn jetty:stop" and "mvn jetty:run"

#### Eclipse

- clone project from https://github.com/scg-unibe-ch/ese2016-team3 
- import git project to eclipse (import -> git -> projects from git), then create maven project (import -> Maven -> existing maven project)
- create new enter jetty:stop jetty:run under (run configurations -> Maven Build -> New_configuration -> Goals:)
- run project as New_configuration

## Folder Structure

- **src**: contains all java files, pages (.jsp) and javascript files, which are used to create the page. In addition, the tests are contained
- **log**: contains logs with the activity of users on the website
- **settings**: project specific eclipse settings

