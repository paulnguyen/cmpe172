# Setup DVWA

## Start DVWA Docker Container

```
docker run --name dvwa --network attack --rm -td -p 80:80 vulnerables/web-dvwa
```

## Web Browser Login

```
URL: http://localhost/login.php
User: admin
Password: password
```

### Setup Up DVWA Database

* Click on:  "Create/Reset Database"
* Login Again: http://localhost/login.php


### DVWA Shell / DB Access

```
docker exec -it dvwa bash 
# mysql -u root
> show databases ;
> use dvwa ;
> show tables ;
```











