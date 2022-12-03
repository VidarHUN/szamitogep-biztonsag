# Backend

## Generate keys

```console
openssl genrsa -out domain.key 2048
openssl req -new -key domain.key -out domain.csr
openssl x509 -req -days 365 -in domain.csr -signkey domain.key -out domain.crt
```

Prompt these params:

```console
➜ openssl genrsa -out domain.key 2048
openssl req -new -key domain.key -out domain.csr
openssl x509 -req -days 365 -in domain.csr -signkey domain.key -out domain.crt
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:HU
State or Province Name (full name) [Some-State]:Budapest
Locality Name (eg, city) []:Budapest
Organization Name (eg, company) [Internet Widgits Pty Ltd]:hitmit
Organizational Unit Name (eg, section) []:dev
Common Name (e.g. server FQDN or YOUR name) []:localhost
Email Address []:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:secret_password
An optional company name []:
Certificate request self-signature ok
subject=C = HU, ST = Budapest, L = Budapest, O = hitmit, OU = dev, CN = localhost

```

## Install dependencies

```console
pip install -r requirements.txt
```

## Start uWSGI

```console
uwsgi --master --https 0.0.0.0:8443,domain.crt,domain.key --mount /=server:app
```

### Without HTTPS

```
uwsgi --http-socket 127.0.0.1:5683 --mount /=server:app
```

## Test with the client

```
python client.py
```