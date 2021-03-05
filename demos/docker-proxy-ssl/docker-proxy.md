* https://omarghader.github.io/haproxy-letsencrypt-docker-certbot-certificates
* https://github.com/nginx-proxy/nginx-proxy
* https://github.com/nginx-proxy/docker-letsencrypt-nginx-proxy-companion
* https://www.keycdn.com/blog/best-free-dns-hosting-providers


## HAProxy LetsEncrypt Docker Tutorial

* https://omarghader.github.io/haproxy-letsencrypt-docker-certbot-certificates/

sudo apt-get install openssl 
openssl req -nodes -x509 -newkey rsa:2048 -keyout test.key -out test.crt -days 30
cat test.key test.crt > ./certs/test.pem

```
Generating a 2048 bit RSA private key
.......+++
...................+++
writing new private key to 'test.key'
-----
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) []:US
State or Province Name (full name) []:California
Locality Name (eg, city) []:San Jose
Organization Name (eg, company) []:Gumball, Inc.
Organizational Unit Name (eg, section) []:
Common Name (eg, fully qualified host name) []:gumball.nguyenresearch.com
Email Address []:
pnguyen@macbook LAB4 % 
```

./create-cert.sh gumball.nguyenresearch.com paul@nguyenresearch.com  
./renew-cert.sh gumball.nguyenresearch.com   


## Debug Notes

* https://cloud.google.com/compute/docs/instances/connecting-advanced#thirdpartytools
* https://cloud.google.com/compute/docs/instances/managing-instance-access
* https://cloud.google.com/compute/docs/instances/managing-instance-access#add_oslogin_keys

C02WK6QVHV2L:.ssh paul_nguyen$ pwd
/Users/paul_nguyen/.ssh
C02WK6QVHV2L:.ssh paul_nguyen$ ls
config		id_rsa		id_rsa.pub	known_hosts	macbox		macbox.pub
C02WK6QVHV2L:.ssh paul_nguyen$ cat id_rsa.pub 
ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDFCRV9m3povCjUFdJxz1J4H78hQcGtBIIeonDhuzfDzZVKrHAKPRN9nu0c9dua5KEHgaR1yZWFA2Bq59BXZlsMrHYZ3AKF8RxgdwDjd+Po4zANcxEs13W9vcz/rtmZL4utAae0YiwS/vaHgP+jgC6Ij4pOCm+05dudOPyUi/rcPi6aSCCiR/+NvvOs3CJsB2Fa13CzUmXKnbzHjrN/C6gtBPMRxRFAFLmBRdNXKIXvHNV9JQ1Hev/xUp8ivlC0THLLLD2edusfRJHbk+1oaa9trA+tui/tLwqgkqxLSRTVda9cMDgo6uYOArLwLWzIFP4vuVe12mHeNdR/LGMjj1Vb paul_nguyen@macbox
C02WK6QVHV2L:.ssh paul_nguyen$ 


* https://cloud.google.com/compute/docs/instances/access-overview
* https://cloud.google.com/compute/docs/instances/managing-instance-access
* https://cloud.google.com/compute/docs/oslogin/manage-oslogin-in-an-org

Add Metadata: enable-oslogin and the value is TRUE (when creating instance)

* https://cloud.google.com/sdk/docs/install
* https://cloud.google.com/compute/docs/instances/adding-removing-ssh-keys#sshkeyformat
* https://cloud.google.com/compute/docs/instances/managing-instance-access#add_oslogin_keys

gcloud auth login
    > note: authorize access via browser
gcloud config set project cmpe172-303004 
    > note: user your project id
gcloud compute os-login ssh-keys add --key-file=id_rsa.pub
    > note: user name =  nguyenresearch_gmail_com

ssh nguyenresearch_gmail_com@<host>



* https://docs.docker.com/engine/install/ubuntu/

1. Update the apt package index and install packages to allow apt to use a repository over HTTPS:

sudo apt-get update

sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common



2. Add Docker’s official GPG key:

$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

Verify that you now have the key with the fingerprint 9DC8 5822 9FC7 DD38 854A  E2D8 8D81 803C 0EBF CD88, by searching for the last 8 characters of the fingerprint.

$ sudo apt-key fingerprint 0EBFCD88

pub   rsa4096 2017-02-22 [SCEA]
      9DC8 5822 9FC7 DD38 854A  E2D8 8D81 803C 0EBF CD88
uid           [ unknown] Docker Release (CE deb) <docker@docker.com>
sub   rsa4096 2017-02-22 [S]

3. Use the following command to set up the stable repository. To add the nightly or test repository, add the word nightly or test (or both) after the word stable in the commands below. Learn about nightly and test channels.

    Note: The lsb_release -cs sub-command below returns the name of your Ubuntu distribution, such as xenial. Sometimes, in a distribution like Linux Mint, you might need to change $(lsb_release -cs) to your parent Ubuntu distribution. For example, if you are using Linux Mint Tessa, you could use bionic. Docker does not offer any guarantees on untested and unsupported Ubuntu distributions.

sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"


 Install Docker Engine

    Update the apt package index, and install the latest version of Docker Engine and containerd, or go to the next step to install a specific version:

     $ sudo apt-get update
     $ sudo apt-get install docker-ce docker-ce-cli containerd.io
     $ sudo apt install docker-compose


* https://docs.docker.com/engine/install/linux-postinstall/

To create the docker group and add your user:

    Create the docker group.

    $ sudo groupadd docker

    Add your user to the docker group.

    $ sudo usermod -aG docker $USER

    Log out and log back in so that your group membership is re-evaluated.

    If testing on a virtual machine, it may be necessary to restart the virtual machine for changes to take effect.

    On a desktop Linux environment such as X Windows, log out of your session completely and then log back in.

    On Linux, you can also run the following command to activate the changes to groups:

    $ newgrp docker 

    Verify that you can run docker commands without sudo.

    $ docker run hello-world






