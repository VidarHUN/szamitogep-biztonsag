# Backend

> Only works with python3.9

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

### Configure SQLite3

```console
sudo apt-get update
sudo apt-get install sqlite3
```

> I did not use windows. If you want to run this on windows please find a way for it.

Create db

```console
cd szamitogep-biztonsag/backend
sqlite3 users.db
# Ctrl+d to exit
```

Set ENVAR for database token and database path, for example:

On windows
```console
$env:SECRET = '004f2af45d3a4e161a7dd2d17fdae47f'
$env:DBPATH = 'sqlite:///D:\\Norbi\\sqlite\\users.db'
```
On linux
```console
export SECRET='004f2af45d3a4e161a7dd2d17fdae47f'
export DBPATH='sqlite:////home/Norbi/sqlite/users.db'
```

> TODO: Use environment variables to this purpose

## Parser binary

You have to copy the parser binary into the backend directory and with chmod give execution rights for every group.

```console
cd parser
git submodule init
git submodule update
make
chmod +x parser
cp parser ../backend
```

## Start without hosting server

```console
python3.9 server.py
```

## Start uWSGI

```console
uwsgi --master --https 0.0.0.0:8443,domain.crt,domain.key --mount /=server:app
```

### Without HTTPS

```
uwsgi --http-socket 0.0.0.0:8443 --mount /=server:app
```

## Test with the client

```
python client.py
```

## Authentication handling

If you have a registered user, you can login with basic authentication with the email and password. For this purpose, add `username` and `password` field to the HTTP header and give the value to them.

Whenever you `login` with a registered user, you will get a JWT token which is used for later authentication. Please store this token in a global variable during the session when the user uses the app.

It can be read out from the `login` response with the key `token`.

After that, most of the API is available only if you put this `token` into the `x-access-tokens` header field.