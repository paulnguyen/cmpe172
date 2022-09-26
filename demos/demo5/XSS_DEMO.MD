
# Cross Site Scripting (XSS) Demo

* https://owasp.org/www-community/attacks/xss/


## Source Code Location
```
docker exec -it dvwa bash 
# cd /var/www/html
# cd /var/www/html/vulnerabilities/xss_s
```


## DVWA Security Settings

* Click on "DVWA Security" (http://localhost/security.php)
* Set Security Level to "Low"



## On "SQL Injection" Web Form, Try

 * Click on "XSS (Stored)" (http://localhost/vulnerabilities/xss_s)
 * Sign Guesbook with Name and Message as follows:

 Name: Hacker One
 Message:  <script>alert("You Are Hacked!")</script>



## Observations


* Notice that the JavaScript alert we just created is now displayed.
* Every Time a user comes to this forum, this XSS exploit will be displayed.
* This exploit can be easily modified to capture cookie/session information 
  for future Man-in-Middle attacks.


