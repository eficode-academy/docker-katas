### Docker volumes

Not everything can be in a container. The whole idea is that you can start, stop and delete the containers without losing data. So if you need to persist data, do it outside of the containers.

So let's go back to our webserver [Nginx](https://hub.docker.com/_/nginx/) again. That server can host static HTML content by running it with a volume to the host machine:
```
$ docker run --name some-nginx -v /some/content:/usr/share/nginx/html:ro -d nginx
```
That will host whatever files are in the `/some/content` folder.

The `:ro` atribute is making the host volume read-only, making sure the container can not edit the files on the host.

So try to do the following on the server:

* `git clone` this repository down to the server and navigate to the `labs/volumes/` folder.
* Try to run the above command with the right folder instead of `/some/content`. You can use the command `pwd` (Print working directory) to display where you are.

This will give you a nginx server running, serving your static files... _But on which port?_

* Run a `docker ps` command to find out if it has any ports forwarded from the host.

Remember the past exercise on port forwarding in Docker.

* Make it host the site on port 8000
* Check that it is running by navigating to the hostname or IP of your server with your browser, and on port 8000.

### More advanced docker commands

Before you go on, use the [Docker command line interface](https://docs.docker.com/engine/reference/commandline/cli/) documentation to try a few more commands:

* While your detached container is running, use the ``docker ps`` command to see what silly name Docker gave your container. **This is one command you're going to use often!**
* While your detached container is still running, look at its logs. Try following its logs and refreshing your browser.
* Stop your detached container, and confirm that it is stopped with the `ps` command.
* Start it again, wait 10 seconds for it to fire up, and stop it again.
* Then delete that container from your system.

> NOTE:
    When running most docker commands, you only need to specify the first few characters of a container's ID.
    For example, if a container has the ID ``df4fd19392ba``, you can stop it with ``docker stop df4``.
    You can also use the silly names Docker provides containers by default, such as ``boring_bardeen``.

### summary

Now you have tried to bind volumes to a container to connect the host to the container.
