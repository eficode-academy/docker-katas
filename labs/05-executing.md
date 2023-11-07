# Executing processes in your container

It you want to examine a running container, but do not want to disturb the running process you can execute another process inside the container with `exec`.

This could be a shell, or a script of some sort. In that way you can debug an existing environment before starting a new up.

## Exercise

In this exercise, we want to change a file in an already running container, by executing a secondary process.

### Step by step

- Spin up a new NGINX container: `docker run -d -p 8000:80 nginx`
- Visit the webpage to make sure that NGINX have been setup correctly.

Step into a new container by executing a bash shell inside the container:

```
docker exec -it CONTAINERNAME bash
```

> :bulb: note that the CONTAINERNAME is the name of the NGINX container you just started.

Inside, we want to edit the `index.html` page, with a cli text editor called [nano](https://www.nano-editor.org/).
Because containers only have the bare minimum installed, we need to first install nano, and then use it:

> :bulb: From the [DockerHub description](https://hub.docker.com/_/nginx) we know that the standard place for HTML pages NGINX serves is in /usr/share/nginx/html

- install nano on the container: `apt-get update && apt-get install -y nano`
- Edit the index html page: `nano /usr/share/nginx/html/index.html`
- Save and exit nano by pressing: `CTRL + O` and `enter` to save and `CTRL + X` to exit Nano
- Revisit the page to check that your edition is in effect.

## Summary

You have tried to start a new process by the `exec` command in order to look around in a container, or to edit something.
You have also seen that terminating any of the the process by `Ctrl+d` that does not have PID 1 will not make the container stop.
