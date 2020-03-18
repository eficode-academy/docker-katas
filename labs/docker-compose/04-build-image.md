# Building docker image using docker-compose

In this excercise we're going to setup [bottlepy](https://bottlepy.org/docs/dev/) application running from our compose service.

Checkout the bottle folder and you should be seeing 3 files:
- app.py
- Dockerfile
- requirements.txt

app.py contains our bottle web service that is running on port 8080 and it uses python3.

requirements.txt has the needed requirements for python to be installed before running the service.

Dockerfile is partially empty and now it's your job to fill it in in order to make the application run.

Application can be started with command `python3 /path/to/app.py` and requirements can be installed with `pip3 install -r /path/to/requirements.txt`.

Your `docker-compose.yml` can look something like this:
```yml
version: '3'
services:
    bottle:
      build: ./bottle/
      ports:
```

Where `build:` is refering the folder what docker container we should build.

To make docker-compose to build you can use command `docker-compose up --build` in order to execute the `build:` configuration.

Try to build your application container and open browser to correct port.

_*Q: What do you see on <domain>:<port>/hello/docker-is-awesome ?*_