# Docker volumes

Not everything can be in a container. The whole idea is that you can start, stop and delete the containers without losing data.

So if you need to persist data, do it outside of the containers.

You have two different ways of mounting data from your container `bind mounts` and `volumes`.

**A bind mount** is the simpler one to understand. It takes a host path `/data` and mounts it inside your container eg. `/opt/app/data`. The positive about bind is that it is easy and connects directly to the host filesystem. The downside is that you need to specify it at runtime with no indication on what mounts a given container has, and that you need to deal with backup,migration etc. in an outside tool.

**A docker Volume** is where you can use a named or unnamed volume to store the external data. You would normally use a volume driver for this, but you can get a host mounted path using the default local volume driver.

In the next section, you will get to try both of them.

## Bind mounts

So let's look at the [Nginx](https://hub.docker.com/_/nginx/) container.
The server itself is of little use, if it cannot access our web content.

We need to create a mapping between the host system, and the container with the `-v` command:

``` bash
docker run --name some-nginx -v /some/content:/usr/share/nginx/html:ro -d nginx
```

That will host whatever files are in the `/some/content` folder on the host.

> The `:ro` attribute is making the host volume read-only, making sure the container can not edit the files on the host.

Try to do the following:

* `git clone` this repository down and navigate to the `labs/volumes/` folder.
* Try to run the above command with the right folder instead of `/some/content`. You can use the command `pwd` (Print working directory) to display where you are.

This will give you a nginx server running, serving your static files... _But on which port?_

* Run a `docker ps` command to find out if it has any ports forwarded from the host.

Remember the past exercise on port forwarding in Docker.

* Make it host the site on port 8000
* Check that it is running by navigating to the hostname or IP with your browser, and on port 8000.

## Volumes

Volumes are entities inside docker, and can be created in three different ways.

* By explicitly creating it with the `docker volume create <volume_name>` command
* By creating a named volume at container creation time with `docker run -d -v DataVolume:/opt/app/data nginx`
* By creating an anonymous volume at container creation time with `docker run -d -v /opt/app/data nginx`

First off, lets try to make a data volume called `data`:

``` bash
docker volume create data
```

Docker creates the volume and outputs the name of the volume created.

If you run `docker volume ls` you will have a list of all the volumes created and their driver:

```outputs
DRIVER              VOLUME NAME
local               data
```

> **Note** we will not go through the different drivers. For more info look at Dockers own [example](https://docs.docker.com/engine/admin/volumes/volumes/#use-a-volume-driver).

You can now use this data volume in all containers. Try to mount it to an nginx server with the `docker run -d -v data:/opt/app/data nginx` command.

Multiple containers can

```bash
docker run -ti --rm -v DataVolume3:/var ubuntu
```

> **Note:** Docker doesn't handle any file locking, so applications must account for the file locking themselves.

> **Note:** If we create a volume at the same time as we create a container and we provide the path to a directory that contains data in the base image, that data will be copied into the volume.

## Tips and tricks

As you have seen, the `-v` flag can both create a bindmount or name a volume depending on the syntax. If the first argument begins with a / or ~/ you're creating a bindmount. Remove that, and you're naming the volume. For example:

* `-v /path:/path/in/container` mounts the host directory, `/path` at the `/path/in/container`
* `-v path:/path/in/container` creates a volume named path with no relationship to the host.

### Sharing data

If you want to share volumes or bindmounts between two containers, then use the `--volumes-from` option for the second container. The parameter maps the mapped volumes from the source container to the container being launched.

## More advanced docker commands

Before you go on, use the [Docker command line interface](https://docs.docker.com/engine/reference/commandline/cli/) documentation to try a few more commands:

* While your detached container is running, use the ``docker ps`` command to see what silly name Docker gave your container. **This is one command you're going to use often!**
* While your detached container is still running, look at its logs. Try following its logs and refreshing your browser.
* Stop your detached container, and confirm that it is stopped with the `ps` command.
* Start it again, wait 10 seconds for it to fire up, and stop it again.
* Then delete that container from your system.

> **NOTE:** When running most docker commands, you only need to specify the first few characters of a container's ID. For example, if a container has the ID ``df4fd19392ba``, you can stop it with ``docker stop df4``. You can also use the silly names Docker provides containers by default, such as ``boring_bardeen``.

## summary

Now you have tried to bind volumes to a container to connect the host to the container.
