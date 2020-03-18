# hello-world

Try running a command with docker-compose:

`docker-compose up`

Your terminal output should look like this:

```bash
ERROR: 
        Can't find a suitable configuration file in this directory or any
        parent. Are you in the right directory?

        Supported filenames: docker-compose.yml, docker-compose.yaml
```
Now we need to create docker-compose.yml as the message suggests.

Open editor and create new file with following content:

```yml
version: '3'
services:
    hello-world:
        image: hello-world:latest
```

Now try to run again `docker-compose up` and you should see following message:

```bash
Starting docker-compose_hello-world_1 ... done
Attaching to docker-compose_hello-world_1
hello-world_1  | 
hello-world_1  | Hello from Docker!
hello-world_1  | This message shows that your installation appears to be working correctly.
hello-world_1  | 
hello-world_1  | To generate this message, Docker took the following steps:
hello-world_1  |  1. The Docker client contacted the Docker daemon.
hello-world_1  |  2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
hello-world_1  |     (amd64)
hello-world_1  |  3. The Docker daemon created a new container from that image which runs the
hello-world_1  |     executable that produces the output you are currently reading.
hello-world_1  |  4. The Docker daemon streamed that output to the Docker client, which sent it
hello-world_1  |     to your terminal.
hello-world_1  | 
hello-world_1  | To try something more ambitious, you can run an Ubuntu container with:
hello-world_1  |  $ docker run -it ubuntu bash
hello-world_1  | 
hello-world_1  | Share images, automate workflows, and more with a free Docker ID:
hello-world_1  |  https://hub.docker.com/
hello-world_1  | 
hello-world_1  | For more examples and ideas, visit:
hello-world_1  |  https://docs.docker.com/get-started/
hello-world_1  | 
docker-compose_hello-world_1 exited with code 0
```

As we can see. The hello-world container (tagged with latest) was started using the the compose file and your docker-compose installation is working!

Compose file explained:

`version: '3'`: defines the API version that docker-compose should use
`services:`:  defines the configuration of the container
`hello-world:`: is now the name of the service
`image: hello-world:latest`: defines the container and its version that we're using for `hello-world` service.

Now re-create using hello-world service to use ubuntu container and also add `command:` for the service with value `echo "Hello from me"`

_*Q: So what did this do?*_
