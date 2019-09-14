# Building your flask application with three different from OS's

Building our flask application with Ubuntu might seem like a bad idea, as Ubuntu is a general purpose OS.

Looking into dockerhub we see that python already have an image we can use.

But after building that image, we see that the size of the image is almost doubled.

We go back again and find `python:3-alpine` based on the alpine OS.

This gives us a 75% reduction from Ubuntu, and ~90% reduction from normal python3 image.

```bash
praqmasofus/flaskapp   python3-alpine      f442f4941c53        9 minutes ago       109MB
praqmasofus/flaskapp   python3             fb4d00f89ef3        10 minutes ago      926MB
praqmasofus/flaskapp   ubuntu              817bf9d87465        8 days ago          435MB
```
