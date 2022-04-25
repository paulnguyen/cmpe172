
# Spring Gumball (Version 3)

Create a "Version 3" of your Spring Gumball (based on your implementation of Version 2 from the last lab).  In Version 3, add "Spring JPA with MySQL" and map Spring Gumball Domain Object to MySQL.

Make the "serialNumber" unique in the Database and pre-configure your Spring Java code to look for a specific Serial Number in the DB based on a Static String of Configuration.

```
class GumballModel {

    private String modelNumber ;
    private String serialNumber ;
    private Integer countGumballs ;
    
}
```

Change the Gumball Controller to update the Gumball Inventory in the DB and also update the HTML view to display the Model Number and Serial Number fetched from the DB.


## Hints on Running Spring Gumball with MySQL in Docker Containers

* https://docs.docker.com/config/containers/container-networking/
* https://docs.docker.com/network/network-tutorial-standalone/
* https://docs.docker.com/network/bridge/


* Sample application.properties file:

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/cmpe172
spring.datasource.username=root
spring.datasource.password=cmpe172
```

* Note that in your application.properties file, the data source connection defaults to "localhost" if there is no setting for MYSQL_HOST environment variablle.  This works when MySQL is running on your local machine, or is published via your Local Docker on the standard port 3306 when you run:

```
docker run -d --name mysql -td -p 3306:3306 -e MYSQL_ROOT_PASSWORD=cmpe172 mysql:8.0
```

* Unfortunately, this will not work when your Spring Gumball App runs inside docker.  This is because each Docker Container is isolated like a "Virtual Machine" from other Containers.  Thus, you must reference the MySQL's HOST Name or IP from the Spring Gumball Container in order to connect to the Database.  Connection to "localhost" from Spring Gumball will not work since there's no MySQL DB running on the same "VM" as the Spring Gumball Container.

* Use the following Docker Run command to start your MySQL Container in an isolated Network named "gumball". Note: create the network if it doesn't exist yet.

```
docker network create --driver bridge gumball
docker run -d --network gumball --name mysql -td -p 3306:3306 -e MYSQL_ROOT_PASSWORD=cmpe172 mysql:8.0
```


* Then, run the Spring Gumball App in Docker on the same "gumball" network passing in an environment setting to thell Spring JPA to connect to MySQL via the hostname "mysql".  The "hostname" for a container is the name it was launched with.

```
docker run --network gumball -e "MYSQL_HOST=mysql" --name spring-gumball -td -p 8080:8080 spring-gumball  
```

* For Docker Compose, you can use the following Manifest.  

```
version: "3"

services:
  mysql:
    image: mysql:8.0
    volumes:
      - /tmp:/tmp
    networks:
      - network   
    ports:
      - 3306    
    networks:
      - network
    environment:
      MYSQL_ROOT_PASSWORD: "cmpe172"
    restart: always     
  gumball:
    image: spring-gumball
    depends_on:
    - mysql    
    volumes:
      - /tmp:/tmp
    networks:
      - network   
    ports:
      - 8080    
    environment:
      MYSQL_HOST: "mysql"
    restart: always     
  lb:
    image: eeacms/haproxy
    depends_on:
    - gumball
    ports:
    - "80:5000"
    - "1936:1936"
    environment:
      BACKENDS: "gumball"
      BACKENDS_PORT: "8080"
      DNS_ENABLED: "true"
      COOKIES_ENABLED: "false"
      LOG_LEVEL: "info"
    networks:
      - network

volumes:
  schemas:
    external: false

networks:
  network:
    driver: bridge
```

## Deploying to Google Cloud SQL & Kubernetes Engine

* https://cloud.google.com/sql/docs/mysql/quickstart
* https://cloud.google.com/sql/docs/mysql/connect-kubernetes-engine
* https://kubernetes.io/docs/tasks/inject-data-application/define-environment-variable-container/
* https://medium.com/@johnjjung/how-to-setup-a-kubernetes-cluster-that-can-connect-to-sql-on-gcp-using-private-ips-c0cd41ea3a4e

### Create Cloud SQL MySQL Instance

* Instance ID:  mysql8
* Password:     cmpe172
* Version:      MySQL 8.0
* Region:       Any
* Zone:         Single Zone
* Machine Type: Lightweight
* Storage:      SSD / 10 GB
* Connections:  Private IP
* Network:      default (VPC Native)
  - May require setting up a private service connnection
  - 1. Enable Service Networking API
  - 2. Use Automatic IP Range


### Alternatively, Install MySQL into a GCE (Google Compute Engine VM)

* https://cloud.google.com/solutions/setup-mysql
* https://cloud.google.com/solutions/mysql-remote-access

* Name:           mysql
* Machine Type:   e2-micro
* OS:             Ubuntu
* OS version:     18.04 LTS

```
sudo apt-get update
sudo apt-get -y install mysql-server
sudo mysql_secure_installation (set password to: cmpe172)

sudo mysql -u root -p
mysql> create database cmpe172 ;
```

### Note the Private IP of your new MySQL DB.  

```
For example:  172.22.16.7
```


### Test MySQL Connection from Jumpbox in GKE Cluster

* Deploy Jumpbox

```
kubectl create -f jumpbox.yaml
kubectl exec -it jumpbox -- /bin/bash

apt-get update
apt-get install curl
apt-get install iputils-ping
apt-get install telnet
apt-get install httpie

```

* Install MySQL Client in Jumpbox

```
apt-get update
apt-get install mysql-client 

mysql -u <user> -p -h <db host> <db name>
mysql -u root -p -h 172.22.16.7
```


### Update your GKE Deployment Manifest to Include the Private IP of MySQL Host

* For Example:  172.22.16.7

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-gumball-deployment
  namespace: default
spec:
  selector:
    matchLabels:
      name: spring-gumball
  replicas: 4 # tells deployment to run 2 pods matching the template
  template: # create pods using pod definition in this template
    metadata:
      # unlike pod.yaml, the name is not included in the meta data as a unique name is
      # generated from the deployment name
      labels:
        name: spring-gumball
    spec:
      containers:
      - name: spring-gumball
        image: paulnguyen/spring-gumball:v3.0
        env:
        - name: MYSQL_HOST
          value: "172.22.16.7"   
        ports:
        - containerPort: 8080
```

### Deploy Gumball to GKE

* For Example:

```
kubectl create -f deployment.yaml --save-config 
kubectl create -f service.yaml
kubectl apply -f ingress.yaml
```

### Check the Logs of one of the PODS

* For Example:

```
kubectl logs -f spring-gumball-deployment-5598bc6859-ncv52
```

### Enable SSL on Load Balancer

* Edit Load Balancer
* Select Front End
* Add Front End IP and Port
* Name: gumball-ssl
* Protocol: HTTPS
* Certificate: Create Google Managed SSL Cert
* Domain: gumball.nguyenresearch.com (for example)

* Note the HTTPS IP:  34.117.55.34 (for example)
* Configure DNS for Domain



# References

## Google Cloud SQL

* Quickstart for Cloud SQL for MySQL:  https://cloud.google.com/sql/docs/mysql/quickstart
* Connecting from Google Kubernetes Engine: https://cloud.google.com/sql/docs/mysql/connect-kubernetes-engine


## Docker Networking

* Docker Contianer Networking:  https://docs.docker.com/config/containers/container-networking/
* Bridge Networking Tutorial:  https://docs.docker.com/network/network-tutorial-standalone/
* How to Use the Bridge Network: https://docs.docker.com/network/bridge/

## MySQL:

* Spring Data JDBC:  https://spring.io/projects/spring-data-jdbc
* Spring Data JPA:  https://spring.io/projects/spring-data-jpa
* MySQL Reference (version 8.0):  https://dev.mysql.com/doc/refman/8.0/en/
* MySQL Docker Image:  https://hub.docker.com/_/mysql
* Connecting from GKE:  https://cloud.google.com/sql/docs/mysql/connect-kubernetes-engine
* MySQL Workbench:  https://www.mysql.com/products/workbench/
  - Get version 8.0.22 from Archives (on Mac)
  - https://downloads.mysql.com/archives/workbench/
* Astah DB Reverse Plug-Inb:  https://astah.net/product-plugins/db-reverse-plug-in/
* DB Schema (Free Edition):  https://dbschema.com/editions.html
* Google Cloud SQL:  https://cloud.google.com/sql
  - Supported Versions:  5.6, 5.7 and 8.0









