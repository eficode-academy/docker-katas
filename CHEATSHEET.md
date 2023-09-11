Source: https://docs.docker.com/get-started/docker_cheatsheet.pdf

## General

- [Docker cli](https://docs.docker.com/engine/reference/commandline/cli/) - **Docker CLI** is the command line interface for Docker
- [Docker Desktop](https://docs.docker.com/desktop) - **Docker Desktop** is available for Mac, Linux, and Windows
- `docker --help` - Get help with Docker. You can use `--help` on all subcommands
- `docker info` - Display system-wide information
- [Docker Docs](https://docs.docker.com) - Check out our docs for information on using Docker

## Containers

- `docker run --name <container_name> <image_name>` - Create and run a container from an image, with a custom name
- `docker run -p <host_port>:<container_port> <image_name>` - Run a container and publish a container’s port(s) to the host
- `docker run -d <image_name>` - Run a container in the background
- `docker start|stop <container_name> (or <container-id>)` - Start or stop an existing container
- `docker rm <container_name>` - Remove a stopped container
- `docker exec -it <container_name> sh` - Open a shell inside a running container
- `docker logs -f <container_name>` - Fetch and follow the logs of a container
- `docker inspect <container_name> (or <container_id>)` - Inspect a running container
- `docker ps` - List currently running containers
- `docker ps --all` - List all docker containers (running and stopped)
- `docker container stats` - View resource usage stats

## Images

- `docker build -t <image_name>` - Build an Image from a Dockerfile
- `docker build -t <image_name> . --no-cache` - Build an Image from a Dockerfile without the cache
- `docker images` - List local images
- `docker rmi <image_name>` - Delete an Image
- `docker image prune` - Remove all unused images


## Docker Registries

The default registry is [Docker Hub](https://hub.docker.com), but you can add more registries.


- `docker login -u <username>` - Login into Docker
- `docker push <username>/<image_name>` - Publish an image to Docker Hub
- `docker search <image_name>` - Search Hub for an image
- `docker pull <image_name>` - Pull an image from Docker Hub
- `docker tag <image_name>:<tag> <username>/<image_name>:<tag>` - Tag an image for a registry