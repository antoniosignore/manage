

## Build

    mvn clean install
    
## Test coverage 
    
    Test are computed at runtime by the Jacoco maven plugin
    
    open in a Browser : agent/target/site/index.html 
    
## Run
    
    Start the application in the following order:
    
    Config --> contains all the services configuration under : resources/shared
    Registry  --> Eureka Registry for service discovery
    Admin  --> Admin Boot to manage the service 
    VCenter-service --> connector to VmWare VCenter
    rsync-service --> rsync sub agent (to be deploied into the DataStream VM)    
    agent --> SNMP-X agent
     
    
    
## Tools link
    
    
    Eureka: http://localhost:8761
    Admin Boot: http://localhost:8078
