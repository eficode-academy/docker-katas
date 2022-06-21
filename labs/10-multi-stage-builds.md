# Multi Stage Builds

## Task: create a tiny go-application container

> NB: requires docker version 17.05

In [multi-stage-build/hello.go](multi-stage-build/hello.go) we have created a small go application that writes out `hello world`.

You want to containerize it. That's easy!

    You don't even have to have go installed,
    because you can just use a `base image`
    that has go!

The [Dockerfile](multi-stage-build/Dockerfile) is already created for you in the same folder.

## Exercise

* Try building the image with `docker build` 
* Try run run it.
    * You should see "Hello world!" printed to the console

## Using Multi Stage Builds

The image we build have both the compiler and the binary, which is too much: the only thing we need is just our binary.

By utilizing multi-stage builds, we can seperate compilation image from running image.

## Exercise

* try `docker image ls`.
    * Could we make it smaller?
    We only need the compiler on build-time,
    since go is a statically compiled language.

* See the following `Dockerfile`, it has two `build stages`,
    wherein the latter stage is using the compiled artifacts from the first,

```Dockerfile
# build stage
FROM golang:1.13.2 AS builder
ADD . /src/
RUN cd /src && go build -o goapp

# final stage
FROM alpine
WORKDIR /app
COPY --from=builder /src/goapp /app/
ENTRYPOINT ./goapp
```

* Replace the original `Dockerfile` content and try building it with a different tag and running it.
    * It should still print "Hello world!" to your console.

* Try inspecting the size with `docker image ls`.

* Compare the size of the two images.
    The latter image should be much smaller,
    since it's just containing the go-application using `alpine` as the `base image`,
    and not the entire `golang`-suite of tools.

You can read more about this on: [Use multi-stage builds - docs.docker.com](https://docs.docker.com/develop/develop-images/multistage-build/)

## Bonus exercise

Since go is a statically compiled language,
    we can actually use `scratch` as the `base image`.

Hint: the "scratch" container has no shell,
    so in the Dockerfile you _also_ need to change the entrypoint from "shell form" to "exec form."
    See: `ENTRYPOINT` under [Dockerfile reference](https://docs.docker.com/engine/reference/builder/).

After building with your new Dockerfile, inspect the size of the images.
    You new image should be even smaller than the alpine example.
