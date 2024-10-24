# Constructing a docker image

Running images others made is useful, but if you want to use docker for your own application, chances are you want to construct an image on your own.

A [Dockerfile](https://docs.docker.com/reference/dockerfile/) is a text file containing a list of commands that the Docker daemon calls while creating an image. The Dockerfile contains all the information that Docker needs to know to run the app; a base Docker image to run from, location of your project code, any dependencies it has, and what commands to run at start-up.

It is a simple way to automate the image creation process. The best part is that the [commands](https://docs.docker.com/reference/dockerfile/) you write in a Dockerfile are _almost_ identical to their equivalent Linux commands. This means you don't really have to learn new syntax to create your own Dockerfiles.

## Dockerfile commands summary

Here's a quick summary of some basic commands we will use in our Dockerfile.

> As a rule of thumb, all commands in CAPITAL LETTERS are intended for the docker engine. E.g.

- FROM # base image
- RUN # run a command
- ADD and COPY # copy files into the image
- CMD # run a command at start-up, but can be overridden
- EXPOSE # expose a port
- ENTRYPOINT # run a command at start-up

<details>
<summary>Click to see the full list of commands</summary>

Details:

- `FROM` is always the first item in the Dockerfile. It is a requirement that the Dockerfile starts with the `FROM` command. Images are created in layers, which means you can use another image as the base image for your own. The `FROM` command defines your base layer. As argument, it takes the name of the image. Optionally, you can add the Docker Hub username of the maintainer and image version, in the format `username/imagename:version`.

- `RUN` is used to build up the image you're creating. For each `RUN` command, Docker will run the command then create a new layer of the image. This way you can roll back your image to previous states easily. The syntax for a `RUN` instruction is to place the full text of the shell command after the `RUN` (e.g., `RUN mkdir /user/local/foo`). This will automatically run in a `/bin/sh` shell. You can define a different shell like this: `RUN /bin/bash -c 'mkdir /user/local/foo'`

- `COPY` copies local files into the container. The files need to be in the same folder (or a sub folder) as the Dockerfile itself. An example is copying the requirements for a python app into the container: `COPY requirements.txt /usr/src/app/`.

- `ADD` should only be used if you want to copy and unpack a tar file into the image. In any other case, use `COPY`. `ADD` can also be used to add a file directly from an URL; consider whether this is good practice.

- `CMD` defines the commands that will run on the image at start-up. Unlike a `RUN`, this does not create a new layer for the image, but simply runs the command. There can only be one `CMD` in a Dockerfile. If you need to run multiple commands, the best way to do that is to have the `CMD` run a script. `CMD` requires that you tell it where to run the command, unlike `RUN`. So example `CMD` commands would be:

```dockerfile
  CMD ["python3", "./app.py"]

  CMD ["/bin/bash", "echo", "Hello World"]
```

- `EXPOSE` creates a hint for users of an image that provides services on ports. It is included in the information which can be retrieved via `$ docker inspect <container-id>`.

> **Note:** The `EXPOSE` command does not actually make any ports accessible to the host! Instead, this requires
> publishing ports by means of the `-p` or `-P` flag when using `$ docker run`.

- `ENTRYPOINT` configures a command that will run no matter what the user specifies at runtime.

> :bulb: this is not the full list of commands, but the ones you will be using in the exercise. For a full list, see the [Dockerfile reference](https://docs.docker.com/engine/reference/builder/).

</details>

## Write a Dockerfile

We want to create a Docker image with a Python web app.

As mentioned above, all user images are based on a _base image_. We will build our own Python image based on [Ubuntu](https://hub.docker.com/_/ubuntu/). We'll do that using a **Dockerfile**.

> :bulb: If you want to learn more about Dockerfiles, check out [Best practices for writing Dockerfiles](https://docs.docker.com/engine/userguide/eng-image/dockerfile_best-practices/).

1. We have made a small boilerplate file and app for you in the [/building-an-image](./building-an-image/) folder, so head over there and add content to the Dockerfile as described below

   We'll start by specifying our base image, using the `FROM` keyword:

   ```docker
   FROM ubuntu:22.04
   ```

1. The next step is usually to write the commands of copying the files and installing the dependencies. But first we will install the Python pip package to the ubuntu linux distribution. This will not just install the pip package but any other dependencies too, which includes the python interpreter. Add the following [RUN](https://docs.docker.com/engine/reference/builder/#run) commands next:

   ```docker
   RUN apt-get update -y
   RUN apt-get install -y python3 python3-pip python3-dev build-essential
   ```

1. Let's add the files that make up the Flask Application.

   Install all Python requirements for our app to run. This will be accomplished by adding the lines:

   ```docker
   COPY requirements.txt /usr/src/app/
   RUN pip3 install --no-cache-dir -r /usr/src/app/requirements.txt
   ```

   Copy the application app.py into our image by using the [COPY](https://docs.docker.com/engine/reference/builder/#copy) command.

   ```docker
   COPY app.py /usr/src/app/

   ```

1. Specify the port number which needs to be exposed. Since our flask app is running on `5000` that's what we'll expose.

   ```docker
   EXPOSE 5000
   ```

   > :bulb: The `EXPOSE` instruction does not actually publish the port.
   > It functions as a type of documentation between the person who builds the
   > image and the person who runs the container,
   > about which ports are intended to be published.
   > You need the `-p`/`-P` command to actually open the host ports.

1. The last step is the command for running the application which is simply - `python3 ./app.py`. Use the [CMD](https://docs.docker.com/engine/reference/builder/#cmd) command to do that:

   ```docker
   CMD ["python3", "/usr/src/app/app.py"]
   ```

   The primary purpose of `CMD` is to tell the container which command it should run by default when it is started.

1. Verify your Dockerfile.

   Our `Dockerfile` is now ready. This is how the file should look:

   ```docker
   # The base image
   FROM ubuntu:22.04

   # Install python and pip
   RUN apt-get update -y
   RUN apt-get install -y python3 python3-pip python3-dev build-essential

   # Install Python modules needed by the Python app
   COPY requirements.txt /usr/src/app/
   RUN pip3 install --no-cache-dir -r /usr/src/app/requirements.txt

   # Copy files required for the app to run
   COPY app.py /usr/src/app/

   # Declare the port number the container should expose
   EXPOSE 5000

   # Run the application
   CMD ["python3", "/usr/src/app/app.py"]
   ```

### Build the image

Now that you have your `Dockerfile`, you can build your image. The `docker build` command does the heavy-lifting of creating a docker image from a `Dockerfile`.

The `docker build` command is quite simple - it takes an optional tag name with the `-t` flag, and the location of the directory containing the `Dockerfile` - the `.` indicates the current directory:

```
docker build -t myfirstapp .
```

Expected output (at the end of the run):

```
[+] Building 79.5s (11/11) FINISHED                                                                                                      docker:default
 => [internal] load build definition from Dockerfile                                                                                               0.1s
 => => transferring dockerfile: 583B                                                                                                               0.0s
 => [internal] load metadata for docker.io/library/ubuntu:22.04                                                                                    1.6s
 => [internal] load .dockerignore                                                                                                                  0.0s
 => => transferring context: 2B                                                                                                                    0.0s
 => [1/6] FROM docker.io/library/ubuntu:22.04@sha256:0e5e4a57c2499249aafc3b40fcd541e9a456aab7296681a3994d631587203f97                              4.9s
 => => resolve docker.io/library/ubuntu:22.04@sha256:0e5e4a57c2499249aafc3b40fcd541e9a456aab7296681a3994d631587203f97                              0.0s
 => => sha256:6414378b647780fee8fd903ddb9541d134a1947ce092d08bdeb23a54cb3684ac 29.54MB / 29.54MB                                                   1.2s
 => => sha256:0e5e4a57c2499249aafc3b40fcd541e9a456aab7296681a3994d631587203f97 6.69kB / 6.69kB                                                     0.0s
 => => sha256:3d1556a8a18cf5307b121e0a98e93f1ddf1f3f8e092f1fddfd941254785b95d7 424B / 424B                                                         0.0s
 => => sha256:97271d29cb7956f0908cfb1449610a2cd9cb46b004ac8af25f0255663eb364ba 2.30kB / 2.30kB                                                     0.0s
 => => extracting sha256:6414378b647780fee8fd903ddb9541d134a1947ce092d08bdeb23a54cb3684ac                                                          3.3s
 => [internal] load build context                                                                                                                  0.0s
 => => transferring context: 469B                                                                                                                  0.0s
 => [2/6] RUN apt-get update -y                                                                                                                    9.1s
 => [3/6] RUN apt-get install -y python3 python3-pip python3-dev build-essential                                                                  54.2s
 => [4/6] COPY requirements.txt /usr/src/app/                                                                                                      0.1s 
 => [5/6] RUN pip3 install --no-cache-dir -r /usr/src/app/requirements.txt                                                                         4.6s 
 => [6/6] COPY app.py /usr/src/app/                                                                                                                0.1s 
 => exporting to image                                                                                                                             4.6s 
 => => exporting layers                                                                                                                            4.6s 
 => => writing image sha256:3c7c10734be5cfd548d5dba13ef5bf788fcc7e2050d1e8ecf4979801fee45ce9                                                       0.0s 
 => => naming to docker.io/library/myfirstapp 

```

If you don't have the `ubuntu:22.04` image, the client will first pull the image and then create your image. If you do have it, your output on running the command will look different from mine.

If everything went well, your image should be ready! Run `docker image ls` and see if your image (`myfirstapp`) shows.

### Run your image

The next step in this section is to run the image and see if it actually works.

```
docker run -p 8080:5000 --name myfirstapp myfirstapp
```

Expected output:

```
 * Running on http://0.0.0.0:5000/ (Press CTRL+C to quit)
```

> :bulb: remember that the application is listening on port 5000 on the Docker virtual network, but on the host it is listening on port 8080.

Head over to `http://<IP>:8080` or your server's URL and your app should be live.

## EXTRA Images and layers

When dealing with docker images, a layer, or image layer, is a change on an image. Every time you run one of the commands RUN, COPY or ADD in your Dockerfile it adds a new layer, causes the image to change to the new layer. You can think of it as staging changes when you're using Git: You add a file's change, then another one, then another one...

Consider the following Dockerfile:

```dockerfile
  FROM ubuntu:22.04
  RUN apt-get update -y
  RUN apt-get install -y python3 python3-pip python3-dev build-essential
  COPY requirements.txt /usr/src/app/
  RUN pip3 install --no-cache-dir -r /usr/src/app/requirements.txt
  COPY app.py /usr/src/app/
  EXPOSE 5000
  CMD ["python3", "/usr/src/app/app.py"]
```

First, we choose a starting image: `ubuntu:22.04`, which in turn has many layers.
We add another layer on top of our starting image, running an update on the system. After that yet another for installing the python ecosystem.
Then, we tell docker to copy the requirements to the container. That's another layer.

The concept of layers comes in handy at the time of building images. Because layers are intermediate images, if you make a change to your Dockerfile, docker will build only the layer that was changed and the ones after that. This is called layer caching.

Each layer is build on top of it's parent layer, meaning if the parent layer changes, the next layer does as well.

If you want to concatenate two layers (e.g. the update and install [which is a good idea](https://docs.docker.com/engine/userguide/eng-image/dockerfile_best-practices/#run)), then do them in the same RUN command:

```dockerfile
FROM ubuntu:22.04
RUN apt-get update && apt-get install -y \
 python3 \
 python3-pip \
 python3-dev \
 build-essential
COPY requirements.txt /usr/src/app/
RUN pip3 install --no-cache-dir -r /usr/src/app/requirements.txt
COPY app.py /usr/src/app/
EXPOSE 5000
CMD ["python3", "/usr/src/app/app.py"]
```

If you want to be able to use any cached layers from last time, they need to be run _before the update command_.

> NOTE:
> Once we build the layers, Docker will reuse them for new builds. This makes the builds much faster. This is great for continuous integration, where we want to build an image at the end of each successful build (e.g. in Jenkins). But the build is not only faster, the new image layers are also smaller, since intermediate images are shared between images.

Try to move the two `COPY` commands before for the `RUN` and build again to see it taking the cached layers instead of making new ones.

### Delete your image

If you make a `docker ps -a` command, you can now see a container with the name _myfirstapp_ from the image named _myfirstapp_.

```
docker ps -a
```

Expected output (you might have more containers):

```
CONTAINER ID        IMAGE                     COMMAND                  CREATED              STATUS                      PORTS                                                          NAMES
fcfba2dfb8ee        myfirstapp                "python3 /usr/src/a..."   About a minute ago   Exited (0) 28 seconds ago                                                                  myfirstapp
```

Make a `docker image ls` command to see that you have a docker image with the name `myfirstapp`

Try now to first:

- remove the container
- remove the image file as well with the `image rm` [command](https://docs.docker.com/engine/reference/commandline/image_rm/).
- make `docker image ls` again to see that it's gone.

## Instructions

There are constantly getting added new keywords to the Dockerfile. You can find a list of all the keywords [here](https://docs.docker.com/engine/reference/builder/).

## Summary

You learned how to write your own docker images in a `Dockerfile` with the use of the `FROM` command to choose base-images like Alpine or Ubuntu and keywords like `RUN` for executing commands,`COPY` to add resources to the container, and `CMD` to indicate what to run when starting the container.
You also learned that each of the keywords generates an image layer on top of the previous, and that everyone of the layers can be converted to a running container.
