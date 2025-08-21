# Empty image (useless, but really empty)

In this example, we demonstrate that Scratch is, in fact, an empty image.

It is not possible to `docker pull scratch` because the image does not actually exist as a
downloadable entity.

Neither is it possible to simply use `FROM scratch` in a Dockerfile without additional context.

But we can do the following:

```dockerfile
FROM scratch
COPY emptyfile .
```

and then `docker build -t empty:latest .`

We can now use `docker image ls` to see that the created image is empty.
