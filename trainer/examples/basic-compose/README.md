# Simple single container compose example

## Build image

```
docker build -t basic-compose:latest .
```

## Run image manually

```
docker run --rm -p 8050:5000 -v $(pwd):/usr/src/app -d basic-compose:latest /usr/src/app/app.py
```

and show at [localhost:8050](http://localhost:8050) that it is running.

## Run with compose

Show compose content

Run with:
`docker compose up` and demo that it is running, then stop again with `docker compose down`.

Then show again with `docker compose up -d`
