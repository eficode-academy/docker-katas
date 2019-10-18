# Build and run instructions for systemd based containers:

## Build:
```
docker build -t local/centos7:mailserver
```

## Run:
```
docker run -p 80:80 -p 25:25 \
  -v /sys/fs/cgroup:/sys/fs/cgroup:ro \
  --tmpfs /run \
  -d local/centos7:mailserver
```

# Build and run using docker-compose:
There is a docker-compose.yml, which can be used to build and run this container image. Of-course a Dockerfile is a pre-requisite.

```
docker-compose build

docker-compose up -d
```

## Verify:

```
$ docker ps
CONTAINER ID        IMAGE                COMMAND             CREATED             STATUS              PORTS                                                                                                                                                  NAMES
604a437cc577        systemd_mailserver   "/usr/sbin/init"    2 minutes ago       Up 2 minutes        0.0.0.0:25->25/tcp, 0.0.0.0:80->80/tcp, 0.0.0.0:110->110/tcp, 0.0.0.0:143->143/tcp, 0.0.0.0:443->443/tcp, 0.0.0.0:993->993/tcp, 0.0.0.0:995->995/tcp   systemd_mailserver_1
```


