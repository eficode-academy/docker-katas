# Docker Secrets

Applications often require access to access tokens, passwords, and other sensitive information.  Shipping this configuration in images poses security challenges, not to mention that with containers, applications are now dynamic and portable across multiple environments, making this a poor fit.

Docker secrets provide a means of managing sensitive information required at runtime independently of the build and run process.

> ## Store config in the environment
> An app’s config is everything that is likely to vary between deploys (staging, production, developer environments, etc). This includes:
>
> * Resource handles to the database, Memcached, and other backing services
> * Credentials to external services such as Amazon S3 or Twitter
> * Per-deploy values such as the canonical hostname for the deploy
> [https://12factor.net/config]

## Starting with Docker Secrets

To get started, we need to be running docker in swarm mode.  Swarm is the distributed orchestration tool that originally shipped with docker, which now is being replaces with kubernetes.

The first step is to set up swarm mode on your docker host:

    docker swarm init

You can see initially that there are no secrets being managed with this command:

    $ docker secret ls
    ID                  NAME                DRIVER              CREATED             UPDATED
    $

We can create a secret like this:

    $ printf "docker1234" | docker secret create db_pwd -
    w3yszkcy3ip08cgnfbiggq5e6
    $ docker secret ls
    ID                          NAME                DRIVER              CREATED             UPDATED
    w3yszkcy3ip08cgnfbiggq5e6   db_pwd                                  6 seconds ago       6 seconds ago
    $


Now we want to grant access to our secret:

    $ docker service  create --name redis --secret db_pwd redis:alpine

And we can verify that the secret is available like this:

    $ docker ps --filter name=redis -q
    5cb1c2348a59
    $ docker container exec $(docker ps --filter name=redis -q) ls -l /run/secrets
    $ docker container exec $(docker ps --filter name=redis -q) cat /run/secrets/db_pwd

Verify it doesn't commit

    $ docker commit $(docker ps --filter name=redis -q) committed_redis
    $ docker run --rm -it committed_redis cat /run/secrets/my_secret_data

Try removing the secret. The removal fails because the redis service is running and has access to the secret.


    $ docker secret ls
    ID                          NAME                DRIVER              CREATED             UPDATED
    w3yszkcy3ip08cgnfbiggq5e6   db_pwd                                  6 seconds ago       6 seconds ago
    $ docker secret rm db_pwd

    Error response from daemon: rpc error: code = 3 desc = secret
    'db_pwd' is in use by the following service: redis



Remove access to the secret from the running redis service by updating the service.

    $ docker service update --secret-rm db_pwd redis

Cleanup the service, secret and leave swarm mode:

    $ docker service rm redis
    $ docker secret rm db_pwd
    $ docker swarm leave --force


## Cheatsheet

| Command | Usage |
| --- | --- |
| docker secret create | Create a secret from a file or STDIN as content |
| docker secret inspect |Display detailed information on one or more secrets |
| docker secret ls | List secrets |
| docker secret rm | Remove one or more secrets |


## Slides (later)

High level overview - why secrets, diagram of how

## Additonal exercises

* [Docker lab on secrets](https://github.com/docker/labs/tree/master/security/secrets)
