# Getting started

In this section you will install Docker and run your very first container.

## Terminology

In this section, you will see a lot of Docker-specific jargon which might be confusing to some. So before you go further, let's clarify some terminology that is used frequently in the Docker ecosystem.

- *Images* - The file system and configuration of our application which are used to create containers. To find out more about a Docker image, run `docker image inspect alpine`. If you do not have the `alpine` image this command will fail. You can use the `docker image pull` command to download the **alpine** image. When you executed the command `docker container run hello-world`, it also did a `docker image pull` behind the scenes to download the **hello-world** image.
- *Containers* - Running instances of Docker images &mdash; containers run the actual applications. A container includes an application and all of its dependencies. It shares the kernel with other containers, and runs as an isolated process in user space on the host OS. You created a container using `docker container run` based on an image that was downloaded. A list of running containers can be seen using the `docker container ls` command.
- *Docker daemon* - The background service running on the host that manages building, running and distributing Docker containers.
- *Docker client* - The command line tool that allows the user to interact with the Docker daemon.
- *Docker Hub* - A [registry](https://hub.docker.com/explore/) of Docker images. You can think of the registry as a directory of all available Docker images. You'll be using this later in this tutorial.

## Installing Docker

Depending on what OS you are running, installation is different, but head over to the [Community Edition](https://www.docker.com/community-edition) website and follow the instructions there.
