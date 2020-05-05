# Multistage build with java app

Building a java application with the `gradle` container,
and copying it into `java`.

## Build

```shell
$ docker build -t java-app:single .
...
Successfully tagged java-app:single
```

And build the multi stage dockerfile as well:

```shell
$ docker build -t java-app:multi -f Dockerfile-multistage .
...
Successfully tagged java-app:multi
```

## Show the image size

```shell
$ docker image ls
REPOSITORY                                      TAG                 IMAGE ID            CREATED             SIZE
java-app                                        multi               d2f488502c4d        22 seconds ago      259MB
java-app                                        single              0e8df415465d        3 minutes ago       701MB

```
