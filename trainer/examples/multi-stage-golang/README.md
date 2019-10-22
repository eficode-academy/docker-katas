# Multistage build with go app

Building a go application with the `golang` container,
and copying it into `scratch`.

## Build

```shell
$ docker build -t golang-scratch .
...
Successfully tagged golang-scratch:latest
```

## Run the image

```shell
$ docker run golang-scratch
hello world
```

## Show the image size

```shell
$ docker image ls | grep golang
golang-scracth    latest              9330bf30dfe5        2 minutes ago       2.01MB
golang            1.13.2-buster       e37698ff7351        2 days ago          803MB
```
