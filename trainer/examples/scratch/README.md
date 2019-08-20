# Empty image (useles, but really empty)

In this example we show that Scratch is in fact empty.

It is not possible to `docker pull scratch` as the image doesn't actualy exist.

Neither is it possible ot just do a `FROM scratch` Dockerfile.

But we can do the following:

```
FROM scratch
COPY emptyfile .
```

and then `docker build -t empty:latest .`

We can now use `docker image ls` to see that the created image is empty.
