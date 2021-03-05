#!/bin/bash

set -e

echo "Starting create new certificate..."
if [ "$#" -lt 2 ]; then
    echo "Usage: ...  <domain> <email> [options]"
    exit
fi

DOMAIN=$1
EMAIL=$2
OPTIONS=$3

docker run --rm \
  -v $PWD/letsencrypt:/etc/letsencrypt \
  -v $PWD/webroot:/webroot \
  certbot/certbot \
  certonly --webroot -w /webroot \
  -d $DOMAIN \
  --email $EMAIL \
  --non-interactive \
  --agree-tos \
  $3

# Merge private key and full chain in one file and add them to haproxy certs folder
function cat-cert() {
  dir="./letsencrypt/live/$1"
  cat "$dir/privkey.pem" "$dir/fullchain.pem" > "./certs/$1.pem"
}

# Run merge certificate for the requested domain name
cat-cert $DOMAIN

