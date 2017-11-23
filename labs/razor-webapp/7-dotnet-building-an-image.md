# Constructing a docker image

Running containers others made is useful, but if you want to use docker for production, chances are you want to construct a container on your own.

### Dockerfile commands summary

Here's a quick summary of the few basic commands we will use in our Dockerfile.

- FROM
- RUN
- ADD and COPY
- CMD
- EXPOSE

* `FROM` is always the first item in the Dockerfile. It is a requirement that the Dockerfile starts with the `FROM` command. Images are created in layers, which means you can use another image as the base image for your own. The `FROM` command defines your base layer. As argument, it takes the name of the image. Optionally, you can add the Docker Hub username of the maintainer and image version, in the format `username/imagename:version`.

* `RUN` is used to build up the image you're creating. For each `RUN` command, Docker will run the command then create a new layer of the image. This way you can roll back your image to previous states easily. The syntax for a `RUN` instruction is to place the full text of the shell command after the `RUN` (e.g., `RUN mkdir /user/local/foo`). This will automatically run in a `/bin/sh` shell. You can define a different shell like this: `RUN /bin/bash -c 'mkdir /user/local/foo'`

* `COPY` copies local files into the container. The files need to be in the same folder (or a sub folder) as the Dockerfile itself. An example is copying the requirements for a python app into the container: `COPY requirements.txt /usr/src/app/`.

* `CMD` defines the commands that will run on the image at start-up. Unlike a `RUN`, this does not create a new layer for the image, but simply runs the command. There can only be one `CMD` in a Dockerfile. If you need to run multiple commands, the best way to do that is to have the `CMD` run a script. `CMD` requires that you tell it where to run the command, unlike `RUN`. So example `CMD` commands would be:
```
  CMD ["python", "./app.py"]

  CMD ["/bin/bash", "echo", "Hello World"]
```

* `EXPOSE` creates a hint for users of an image that provides services on ports. It is included in the information which can be retrieved via `$ docker inspect <container-id>`.

>**Note:** The `EXPOSE` command does not actually make any ports accessible to the host! Instead, this requires
publishing ports by means of the `-p` or `-P` flag when using `$ docker run`.

## Write a Dockerfile
We want to create a Docker image with a .NET web app.

As mentioned above, all user images are based on a _base image_. Since our application is written in C#, we will build our own .NET image based on [the official microsoft image](https://hub.docker.com/r/microsoft/dotnet/). We'll do that using a **Dockerfile**.

>**Note:** If you want to learn more about Dockerfiles, check out [Best practices for writing Dockerfiles](https://docs.docker.com/engine/userguide/eng-image/dockerfile_best-practices/).

A [Dockerfile](https://docs.docker.com/engine/reference/builder/) is a text file containing a list of commands that the Docker daemon calls while creating an image. The Dockerfile contains all the information that Docker needs to know to run the app; a base Docker image to run from, location of your project code, any dependencies it has, and what commands to run at start-up.

It is a simple way to automate the image creation process. The best part is that the [commands](https://docs.docker.com/engine/reference/builder/) you write in a Dockerfile are *almost* identical to their equivalent Linux commands. This means you don't really have to learn new syntax to create your own Dockerfiles.


1. Create a file called **Dockerfile**, and add content to it as described below. We have made a small boilerplate file and app for you in the [/4](./4/) folder, so head over there.

  We'll start by specifying our base image, using the `FROM` keyword:

  ```
  FROM microsoft/dotnet:latest
  ```

2. The next step is usually to write the commands of copying the files and installing the dependencies:
 ```
  COPY RazorPages /app
```

3. Since we made our own folder, we need to change to that folder to execute work. Then we restore packages.
```
WORKDIR /app
RUN dotnet restore
```

4. Specify the port number which needs to be exposed. Since our app is running on `5000` that's what we'll expose.
  ```
  EXPOSE 5000
  ```

5. .NET is a bit special, in the sense that it needs an environmental variable to be set, otherwise the container will not accept traffic. This is setup automatically for most environments, but since Docker is a stripped down environment we have to add the ENV variable: 

```
ENV ASPNETCORE_URLS http://*:5000
```

6. The last step is the command for running the application which is simply - `dotnet run`. Use the [CMD](https://docs.docker.com/engine/reference/builder/#cmd) or [ENTRYPOINT](https://docs.docker.com/engine/reference/builder/#entrypoint) command to do that:

  ```
  ENTRYPOINT dotnet run
  ```

Which will spin our app up when starting the container. 

6. Verify your Dockerfile.

  Our `Dockerfile` is now ready. This is how it looks:

  ```
  # The base image
  FROM microsoft/dotnet:latest

  # Copy files required for the app to run
  COPY RazorPages /app
  # Change to the correct working directory
  WORKDIR /app
  # Nuget restore
  RUN dotnet restore

  # Dotnet specific, expose url
  ENV ASPNETCORE_URLS http://*:5000
  # Declare the port number the container should expose
  EXPOSE 5000

  # Run the application
  ENTRYPOINT dotnet run
  ```

### Build the image

Now that you have your `Dockerfile`, you can build your image. The `docker build` command does the heavy-lifting of creating a docker image from a `Dockerfile`.

The `docker build` command is quite simple - it takes an optional tag name with the `-t` flag, and the location of the directory containing the `Dockerfile` - the `.` indicates the current directory:

```
$ docker build -t myfirstapp .
Sending build context to Docker daemon  3.031MB
Step 1/6 : FROM microsoft/dotnet:latest
 ---> 84b39efffa19
Step 2/6 : COPY RazorPages /app
 ---> 5d0f81b1a1b4
Removing intermediate container a8d0a1bd2f51
Step 3/6 : WORKDIR /app
 ---> c51fac4a889a
Removing intermediate container ffa771d78441
Step 4/6 : RUN dotnet restore
 ---> Running in 13f9b7351018
  Restoring packages for /app/RazorPages.csproj...
  Restoring packages for /app/RazorPages.csproj...
  <Snip nuget restore>
  Restore completed in 6.53 sec for /app/RazorPages.csproj.
 ---> ecd0224ab887
Removing intermediate container 13f9b7351018
Step 5/6 : EXPOSE 5000
 ---> Running in e0ffd1c407a7
 ---> 9b1682155639
Removing intermediate container e0ffd1c407a7
Step 6/7 : ENV ASPNETCORE_URLS http://*:5000
 ---> Running in e607b242136f
 ---> fc032d4d8142
Removing intermediate container e607b242136f
Step 7/7 : ENTRYPOINT dotnet run
 ---> Running in e4d6027f13a8
 ---> 24b2ad3fe895
Removing intermediate container e4d6027f13a8
Successfully built 6465c8827ed5
Successfully tagged testing:latest


```

If you don't have the base image image, the client will first pull the image and then create your image. If you do have it, your output on running the command will look different from mine.

If everything went well, your image should be ready! Run `docker images` and see if your image (`myfirstapp`) shows.

### Run your image
The next step in this section is to run the image and see if it actually works.

```
$ docker run -p 8888:5000 --name myfirstapp myfirstapp
 * Running on http://0.0.0.0:5000/ (Press CTRL+C to quit)
```

Head over to `http://localhost:8888` or your server's URL and your app should be live.

### Delete your image

If you make a `docker ps -a` command, you can now see a container with the name *myfirstapp* from the image named *myfirstapp*.
```
sofus@Praq-Sof:/4$ docker ps -a
CONTAINER ID        IMAGE                     COMMAND                  CREATED              STATUS                      PORTS                                                          NAMES
fcfba2dfb8ee        myfirstapp                "dotnet run .."   About a minute ago   Exited (0) 28 seconds ago                                                                  myfirstapp
```
Make a `docker images` command to see that you have a docker image with the name `myfirstapp`

Try now to first:
- remove the container
- remove the image file as well with the `rmi` [command](https://docs.docker.com/engine/reference/commandline/rmi/).
- make `docker images` again to see that it's gone.

## Images
When dealing with docker images, a layer, or image layer is a change on an image, or an intermediate image. Every command you specify (FROM, RUN, COPY, etc.) in your Dockerfile causes the previous image to change, thus creating a new layer. You can think of it as staging changes when you're using Git: You add a file's change, then another one, then another one...

Consider the following Dockerfile:

```
  FROM ubuntu:latest
  RUN apt-get update -y
  RUN apt-get install -y python-pip python-dev build-essential
  COPY requirements.txt /usr/src/app/
  RUN pip install --no-cache-dir -r /usr/src/app/requirements.txt
  COPY app.py /usr/src/app/
  EXPOSE 5000
  CMD ["python", "/usr/src/app/app.py"]
```
First, we choose a starting image: `ubuntu:latest`, which in turn has many layers.
We add another layer on top of our starting image, running an update on the system. After that yet another for installing the python ecosystem.
Then, we tell docker to copy the requirements to the container. That's another layer.

The concept of layers comes in handy at the time of building images. Because layers are intermediate images, if you make a change to your Dockerfile, docker will build only the layer that was changed and the ones after that. This is called layer caching.

Each layer is build on top of it's parent layer, meaning if the parent layer changes, the next layer does as well.

If you want to concatenate two layers (e.g. the update and install [which is a good idea](https://docs.docker.com/engine/userguide/eng-image/dockerfile_best-practices/#run)), then do them in the same RUN command:

```
FROM ubuntu:latest
RUN apt-get update && apt-get install -y \
 python-pip \
 python-dev \
 build-essential
COPY requirements.txt /usr/src/app/
RUN pip install --no-cache-dir -r /usr/src/app/requirements.txt
COPY app.py /usr/src/app/
EXPOSE 5000
CMD ["python", "/usr/src/app/app.py"]
```
If you want to be able to use any cached layers from last time, they need to be run _before the update command_.

> NOTE:
  Once we build the layers, Docker will reuse them for new builds. This makes the builds much faster. This is great for continuous integration, where we want to build an image at the end of each successful build (e.g. in Jenkins). But the build is not only faster, the new image layers are also smaller, since intermediate images are shared between images.

Try to move the two `COPY` commands before for the `RUN` and build again to see it taking the cached layers instead of making new ones.

Now that you are familiar with making a Dockerfile, building it and running it, let us head over to [exercise 5](./5.md) to learn a little more about images and sharing of Dockerfiles.


## Every layer can be a container

As stated above, all FROM, RUN, ADD, COPY, CMD and EXPOSE will create a new layer in your image.

Take a look again at some of the output from building the image above: 

``` 
 ---> c1f2dc732c7c
Removing intermediate container f92f9c719287
Step 6/8 : COPY app.py /usr/src/app/
 ---> 6ed47d3c544a
Removing intermediate container 61a68a949d68
Step 7/8 : EXPOSE 5000
 ---> Running in 1f939928b7d5
 ---> 6c14a93b72f2
 ```

So what docker actually does is 
* Taking the layer created just before
* make a container based of it
* run the command given
* save the layer.

Untill all the commands have been made.
Try to create a container from your `COPY app.py /usr/src/app/` command. 
The id of the layer will likely be different than the example above.

`docker container run -ti 6ed47d3c544a bash`.

You are now in a container run from _that_ layer in the build script. You can't make the `EXPOSE` command, but you can look around, and run the last python app:

```
root@cc5490748b2a:/# ls
bin  boot  dev  etc  home  lib  lib64  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var

root@cc5490748b2a:/# ls /usr/src/app/
app.py  requirements.txt

root@cc5490748b2a:/# python /usr/src/app/app.py
* Running on http://0.0.0.0:5000/ (Press CTRL+C to quit)


```

