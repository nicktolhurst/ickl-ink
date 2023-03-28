#!/bin/bash

domains=(ickl.ink)
local_domain="local.ickl.ink"
rsa_key_size=4096
data_path="./data/certbot"
email="nicholastolhurst@outlook.com"
etc_hosts="/etc/hosts"

if [ "$(id -u)" -ne 0 ]; then
    echo "This script must be run as root or with sudo."
    exit 1
fi

if [ -d "$data_path" ]; then
  read -p "Existing data found for $domains. Continue and replace existing certificate? (y/N) " decision
  if [ "$decision" != "Y" ] && [ "$decision" != "y" ]; then
    exit
  fi
fi

if [ ! -e "$data_path/conf/options-ssl-nginx.conf" ] || [ ! -e "$data_path/conf/ssl-dhparams.pem" ]; then
  echo "### Downloading recommended TLS parameters ..."
  mkdir -p "$data_path/conf"
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf > "$data_path/conf/options-ssl-nginx.conf"
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem > "$data_path/conf/ssl-dhparams.pem"
  echo
fi

echo "### Creating dummy certificate for $domains ..."
path="/etc/letsencrypt/live/$domains"
local_path="$data_path/conf/live/$domains"
mkdir -p "$local_path"
docker compose run --rm --entrypoint "\
  openssl req -x509 -nodes -newkey rsa:$rsa_key_size -days 365\
    -keyout '$path/privkey.pem' \
    -out '$path/fullchain.pem' \
    -addext "subjectAltName=DNS:local.ickl.ink" \
    -subj '/CN=local.ickl.ink'" certbot
echo

echo "### Copying certificate to ca-trust source ..."
cp "$local_path/fullchain.pem" /etc/pki/ca-trust/source/anchors/
echo

echo "### Updating ca-trust ..."
update-ca-trust
echo

ip_exists=$(grep -w "127.0.0.1" "$etc_hosts")

if [ -z "$ip_exists" ]; then
    # Add the IP address and hostname to the hosts file
    echo "127.0.0.1 $local_domain" >> "$etc_hosts"
    echo "Added 127.0.0.1 $local_domain to $etc_hosts."
else
    # Check if the hostname is already present for the IP address
    hostname_exists=$(echo "$ip_exists" | grep -w "$local_domain")

    if [ -z "$hostname_exists" ]; then
        # Add the hostname to the existing IP address entry
        TEMP_FILE=$(mktemp)
        sed "/127.0.0.1/s/$/ $local_domain/" "$etc_hosts" > "$TEMP_FILE"
        cat "$TEMP_FILE" > "$etc_hosts"
        rm "$TEMP_FILE"
        echo "Added $local_domain to the existing IP address entry in $etc_hosts."
    else
        echo "The hostname $local_domain is already present for the IP address 127.0.0.1 in $etc_hosts."
    fi
fi

echo "### Starting nginx ..."
docker compose up --force-recreate -d nginx
echo
