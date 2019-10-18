# Traefik proxy running as independent docker-compose app:
In this example, Traefik runs as an independent docker-compose application. For other applications requiring it's "proxy" services, they need to be on the same network as of the Traefik proxy. The best way is to make a docker bridge network ourselves, and connect all containers on that network. 

## Create a docker bridge network - **services-network**:
```
docker network create services-network
```

## Start Traefik - frond-end / proxy:
Start the Traefik proxy service by briging up it's docker-compose stack - which is compose of just one container!
```
cd proxy
docker-compose up -d
cd ..
```
Note: Please go through the actual `docker-compose.yml` and `traefik.toml` files under the `proxy` directory.


Verify:
```
$ docker ps
CONTAINER ID        IMAGE                      COMMAND                  CREATED             STATUS              PORTS                NAMES
6665c2a9afb2        traefik:1.7                "/traefik"               7 minutes ago       Up 7 minutes        0.0.0.0:80->80/tcp   proxy_traefik_1
```

## Start the  web server - back-end:
```
cd web
docker-compose up -d
cd ..
```
Note: Please go through the actual `docker-compose.yml` file under the `web` directory.


Verify:
```
$ docker ps
CONTAINER ID        IMAGE                      COMMAND                  CREATED             STATUS              PORTS                NAMES
65b42f52017a        praqma/network-multitool   "/docker-entrypoint.…"   2 minutes ago       Up 2 minutes        80/tcp, 443/tcp      web_multitool_1
6665c2a9afb2        traefik:1.7                "/traefik"               7 minutes ago       Up 7 minutes        0.0.0.0:80->80/tcp   proxy_traefik_1
```

## Ensure DNS/name-resolution is in place:
For this example, you can setup the following in your `/etc/hosts` file:
```
$ cat /etc/hosts

127.0.0.1	localhost	localhost.localdomain
. . . 
127.0.0.1	example.com	www.example.com
```

## Check with curl:
```
$ curl example.com
Praqma Network MultiTool (with NGINX) - 65b42f52017a - 172.20.0.3/16

$ curl www.example.com
Praqma Network MultiTool (with NGINX) - 65b42f52017a - 172.20.0.3/16
```

Notice that `curl localhost` will not work, because the proxy is listening on localhost, on port 80, expecting only the DNS names/URLs, which are configured as it's front-ends. It will ignore all other urls , and will show a `404` . 

```
$ curl localhost
404 page not found
```
