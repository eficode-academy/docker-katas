# Sharing containers

Before we can take our dockerized Flask app to another computer, we need to push it up to the Docker Hub so that we (or other people) can pull it down on other machines.

Docker Hub is sort of like GitHub of Docker images. It’s the main place people store their Docker images in the cloud.

First, create an account on the Docker Hub if you haven't already - it's free: https://hub.docker.com/account/signup/

Then, login to that account by running the ``docker login`` command on your laptop.

We're almost ready to push our Flask image up to the Docker Hub. We just need to rename it to our namespace first.

Using the ``docker tag`` command, tag the image you created in the previous section to your namespace. For example, I would run:

```bash
sofus@Praq-Sof:/tmp/4$ docker tag myfirstapp praqmasofus/myfirstapp:latest
```

``myfirstapp`` is the tag I used in my ``docker build`` commands in the previous section, and ``praqmasofus/myfirstapp:`` is the full name of the new Docker image I want to push to the Hub.
`praqmasofus` is my username at dockerhub, and also my namespace for all my images.
The `:latest` is a versioning scheme you can append to.

All that's left to do is push up your image:

```bash
sofus@Praq-Sof:/tmp/4$ docker push praqmasofus/myfirstapp
The push refers to a repository [docker.io/praqmasofus/myfirstapp]
6daf7f1140cb: Pushed
7f74a217d86b: Pushed
09ccfff62b13: Pushed
f83ccb38761b: Pushed
584a7965008a: Pushed
33f1a94ed7fc: Mounted from library/ubuntu
b27287a6dbce: Mounted from library/ubuntu
47c2386f248c: Mounted from library/ubuntu
2be95f0d8a0c: Mounted from library/ubuntu
2df9b8def18a: Mounted from library/ubuntu
latest: digest: sha256:e7016870c297b3c49996ee00972d8abe7f20b4cbe45089dc914193fa894991d3 size: 2407
```

Go to your profile page on the Docker Hub and you should see your new repository listed: https://registry.hub.docker.com/repos/

**Congrats!** You just made your first Docker image and shared it with the world!

Try to pull down the images that your fellows have pushed to see that it's really up there.

For more info on the Docker Hub, and the cli integration, head over to https://docs.docker.com/docker-hub/ and read the guides there.