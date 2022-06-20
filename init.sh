#!/bin/sh
echo "running init.sh script"
export CREDENTIAL_USERNAME=$(cat /var/run/secrets/serviceuser/efsak/username)
export CREDENTIAL_PASSWORD=$(cat /var/run/secrets/serviceuser/efsak/password)
export SRVUSERNAME_EF_PERSONHENDELSE=$(cat /var/run/secrets/serviceuser/personhendelse/username)
export SRVPASSWORD_EF_PERSONHENDELSE=$(cat /var/run/secrets/serviceuser/personhendelse/password)