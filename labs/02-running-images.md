# Running your first container from image

## Learning Goals


- Run an [Alpine Linux](http://www.alpinelinux.org/) container (a lightweight linux distribution) on your system and get a taste of the `docker run` command.

## Introduction

To get started with running your first container from an image, you'll first pull the Alpine Linux image, a lightweight Linux distribution, and then explore various commands to interact with it.

## Exercise

### Overview

- Pull the Alpine Linux image.
- List all images on your system.
- Run a Docker container based on the Alpine image.
- Explore various commands inside the container.
- Understand container naming and IDs.

### Step by step instructions


To get started, let's run the following in our terminal:

* `docker pull alpine`

The `pull` command fetches the alpine **image** from the **Docker registry** and saves it in your system. You can use the `docker image ls` command to see a list of all images on your system.

* `docker image ls`

Expected output (your list of images will look different):

``` bash
REPOSITORY              TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
alpine                  latest              c51f86c28340        4 weeks ago         1.109 MB
hello-world             latest              690ed74de00f        5 months ago        960 B
```

## 1.1 docker run

Let's run a Docker **container** based on this image.

* `docker run alpine ls -l`

Expected output:

```bash
total 48
drwxr-xr-x    2 root     root          4096 Mar  2 16:20 bin
drwxr-xr-x    5 root     root           360 Mar 18 09:47 dev
drwxr-xr-x   13 root     root          4096 Mar 18 09:47 etc
drwxr-xr-x    2 root     root          4096 Mar  2 16:20 home
drwxr-xr-x    5 root     root          4096 Mar  2 16:20 lib
......
......
```

When you run `docker run alpine`, you provided a command (`ls -l`), so Docker started the command specified and you saw the listing.

Try run the following:

* `docker run alpine echo "hello from alpine"`

Expected output:

``` bash
hello from alpine
```

<details>
<summary>More Details</summary>
In this case, the Docker client ran the `echo` command in our alpine container and then exited it. If you've noticed, all of that happened pretty quickly. Imagine booting up a virtual machine, running a command and then killing it. Now you know why they say containers are fast!

</details>

Try another command:

* `docker run alpine /bin/sh`

Wait, nothing happened! Is that a bug? 

Well, no. 

These interactive shells will exit after running any scripted commands, unless they are run in an interactive terminal - so for this example to not exit, you need to add the parameters `i` and `t`.

> :bulb: The flags `-it` are short for `-i -t` which again are the short forms of `--interactive` (Keep STDIN open) and  `--tty` (Allocate a terminal).

* `docker run -it alpine /bin/sh`

You are inside the container shell and you can try out a few commands like `ls -l`, `uname -a` and others. 

* Exit out of the container by giving the `exit` command.

Ok, now it's time to list our containers. 

The `docker ps` command shows you all containers that are currently running.

* `docker ps`

Expected output:

```
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
```

Notice that you have no running containers. When you wrote `exit` in the shell, the primary process (`/bin/sh`) stopped. No containers are running, you see a blank line. Let's try a more useful variant, listing all containers, both stopped and started.

* `docker ps -a`

Expected output:

```
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                      PORTS               NAMES
36171a5da744        alpine              "/bin/sh"                5 minutes ago       Exited (0) 2 minutes ago                        fervent_newton
a6a9d46d0b2f        alpine              "echo 'hello from alp"   6 minutes ago       Exited (0) 6 minutes ago                        lonely_kilby
ff0a5c3750b9        alpine              "ls -l"                  8 minutes ago       Exited (0) 8 minutes ago                        elated_ramanujan
c317d0a9e3d2        hello-world         "/hello"                 34 seconds ago      Exited (0) 12 minutes ago                       stupefied_mcclintock
```

What you see above is a list of all containers that you ran. Notice that the `STATUS` column shows that these containers exited a few minutes ago.

## Naming your container

Take a look again at the output of the `docker ps -a`:

* `docker ps -a`

Expected output:

```
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                      PORTS               NAMES
36171a5da744        alpine              "/bin/sh"                5 minutes ago       Exited (0) 2 minutes ago                        fervent_newton
a6a9d46d0b2f        alpine              "echo 'hello from alp"   6 minutes ago       Exited (0) 6 minutes ago                        lonely_kilby
ff0a5c3750b9        alpine              "ls -l"                  8 minutes ago       Exited (0) 8 minutes ago                        elated_ramanujan
c317d0a9e3d2        hello-world         "/hello"                 34 seconds ago      Exited (0) 12 minutes ago                       stupefied_mcclintock
```

All containers have an **ID** and a **name**. 

Both the ID and name is generated every time a new container spins up with a random seed for uniqueness.

> :bulb: Tip: If you want to assign a specific name to a container then you can use the `--name` option. That can make it easier for you to reference the container going forward.

## Summary

That concludes a whirlwind tour of the `docker run` command which would most likely be the command you'll use most often. It makes sense to spend some time getting comfortable with it. To find out more about `run`, use `docker run --help` to see a list of all flags it supports. As you proceed further, we'll see a few more variants of `docker run`.
