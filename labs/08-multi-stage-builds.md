# Multi Stage Builds

## Task: create a tiny go-application container

In [multi-stage-build/hello.go](multi-stage-build/hello.go) we have created a small go application that prints `hello world` to your terminal.

You want to containerize it - that's easy!

You don't even have to have go installed, because you can just use a `base image` that has go!

The [Dockerfile](multi-stage-build/Dockerfile) is already created for you in the same folder.

## Exercise

- Try building the image with `docker build`
- Try to run it with `docker run`.
- You should see "Hello world!" printed to your terminal.

## Using Multi Stage Builds

The image we built has both the compiler and the compiled binary - which is too much: we only need the binary to run our application.

By utilizing multi-stage builds, we can separate the build stage (compiling) from the image we actually want to ship.

## Exercise

- try `docker image ls`.

- Could we make it smaller? We only need the compiler on build-time, since go is a statically compiled language.

- See the `Dockerfile` below, it has two `build stages`, wherein the latter stage is using the compiled artifact (the binary) from the first:

```Dockerfile
# build stage
FROM golang:1.19 as builder
WORKDIR /app
COPY . /app
RUN go mod download && go mod verify
RUN cd /app && go build -o goapp

# final stage
FROM alpine
WORKDIR /app
COPY --from=builder /app/goapp /app/
ENTRYPOINT ./goapp
```

- Replace the original `Dockerfile` with the one above, and try building it with a different tag.

- When you run it it should still print "Hello world!" to your terminal.

- Try inspecting the size with `docker image ls`.

- Compare the size of the two images. The latter image should be much smaller, since it's just containing the go-application using `alpine` as the `base image`, and not the entire `golang`-suite of tools.

You can read more about this on: [Use multi-stage builds - docs.docker.com](https://docs.docker.com/develop/develop-images/multistage-build/)

You should see a great reduction in size, like in the example below:

```
REPOSITORY            TAG           IMAGE ID       CREATED          SIZE
hello                 golang        5311178b692a   23 seconds ago   805MB
hello                 multi-stage   ba46dc3143ca   2 minutes ago    7.53MB
```

## Bonus Exercise

Since go is a statically compiled language, we can actually use `scratch` as the `base image`.
The `scratch` image is just an empty file system.

Hint: the "scratch" image has no shell, so in the Dockerfile you _also_ need to change the `ENTRYPOINT` from `shell form` to `exec form`.
See: `ENTRYPOINT` under [Dockerfile reference](https://docs.docker.com/engine/reference/builder/).

After building with your new Dockerfile, inspect the size of the images.
Your new image should be even smaller than the alpine-based image!


```Dockerfile
FROM golang:1.19 as builder
WORKDIR /app
COPY . /app
RUN go mod download && go mod verify
RUN cd /app && go build -o goapp
FROM scratch
WORKDIR /app
COPY --from=builder /app/goapp /app/
ENTRYPOINT ["./goapp"]
```