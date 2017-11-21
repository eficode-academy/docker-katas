# Getting started

In this section you will install Docker and run your very first container.

## Terminology
In this section, you will see a lot of Docker-specific jargon which might be confusing to some. So before you go further, let's clarify some terminology that is used frequently in the Docker ecosystem.

- *Images* - The file system and configuration of our application which are used to create containers. To find out more about a Docker image, run `docker inspect alpine`. If you do not have the `alpine` image this command will fail. You can use the `docker pull` command to download the **alpine** image. When you executed the command `docker run hello-world`, it also did a `docker pull` behind the scenes to download the **hello-world** image.
- *Containers* - Running instances of Docker images &mdash; containers run the actual applications. A container includes an application and all of its dependencies. It shares the kernel with other containers, and runs as an isolated process in user space on the host OS. You created a container using `docker run` based on an image that was downloaded. A list of running containers can be seen using the `docker ps` command.
- *Docker daemon* - The background service running on the host that manages building, running and distributing Docker containers.
- *Docker client* - The command line tool that allows the user to interact with the Docker daemon.
- *Docker Hub* - A [registry](https://hub.docker.com/explore/) of Docker images. You can think of the registry as a directory of all available Docker images. You'll be using this later in this tutorial.

## Installing Docker

A couple of other things to remember when using the server:

* Keep in mind that when accessing your containers that serves web-content through a browser, you will need to use the server hostname instead of ``localhost``
* These servers will be terminated shortly after the end of the academy. So if there is anything on the server you wanna save, please do it on the 4th day.

Log in to your server, and follow [the guide on installing docker.](https://store.docker.com/editions/community/docker-ce-server-ubuntu)

By default, Docker commands will require `sudo` to run. If this bothers you, [look for extra instructions on your distro's installation page to create a `docker` user group to fix this.](https://docs.docker.com/engine/installation/linux/linux-postinstall/)
We assume that you have made docker non-sudo. If not, then you need to add sudo to every docker command in the examples.

After Docker finishes installing, you should be ready to go!
