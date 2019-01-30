# Multi Stage Builds

## Task: create a tiny go-application container

> NB: requires docker version 17.05

Imagine you have the following go application.

`hello.go`

```go
package main

import "fmt"

func main() {
    fmt.Println("Hello world!")
}
```

You want to containerize it. That's easy!
    You don't even have to have go installed,
    because you can just use a `base image`
    that has go!

`Dockerfile`

```Dockerfile
FROM golang:alpine
WORKDIR /app
ADD . /app
RUN cd /app && go build -o goapp
ENTRYPOINT ./goapp
```

Try building the image with `docker build` and run it.
    You should see "Hello world!" printed to the console

## Using Multi Stage Builds

Now try `docker image ls`.
    Could we make it smaller?
    We only need the compiler on build-time,
    since go is a statically compiled language.

See the following `Dockerfile`, it has two `build stages`,
    wherein the latter stage is using the compiled artifacts from the first,

```Dockerfile
# build stage
FROM golang:alpine AS builder
ADD . /src/
RUN cd /src && go build -o goapp

# final stage
FROM alpine
WORKDIR /app
COPY --from=builder /src/goapp /app/
ENTRYPOINT ./goapp
```

Try building it with a different tag and running it.
    It should still print "Hello world!" to your console.

Try inspecting the size with `docker image ls`.

Compare the size of the two images.
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
