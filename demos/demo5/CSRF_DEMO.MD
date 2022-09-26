
# Cross Site Request Forgery (CSRF) Demo

* https://owasp.org/www-community/attacks/csrf


## Source Code Location
```
docker exec -it dvwa bash 
# cd /var/www/html
# cd /var/www/html/vulnerabilities/csrf
```


## DVWA Security Settings

* Click on "DVWA Security" (http://localhost/security.php)
* Set Security Level to "Low"



## On "CSRF" Web Form, Try

 * Click on "CSRF" (http://localhost/vulnerabilities/csrf)
 * Set New Password As Follows:

    New password: abc123
    Confirm new password: abc123
    Click Change


## Observations

  * Below the change button you will notice the message that says "Password Changed."
  
  * Notice is the URL string.

```
http://localhost/vulnerabilities/csrf/?password_new=abc123&password_conf=abc123&Change=Change#
```

  * See how the URL string has the below two parameters separated by a "&".
    * password_new=abc123
    * password_conf=abc123

  * This is DVWA's example of bad implementation of how to change a password on a web application for the following reasons:
    * http is being used instead of https, which means this password change was in clear text.
    * An attacker could manipulate the URL string using the address bar or curl to change the password.
    
  * Continue to next step


## Construct an "Attack" Email / HTML Page to trick the user into clicking on the link:

```
http://localhost/vulnerabilities/csrf/?password_new=hacked123&password_conf=hacked123&Change=Change#
```









