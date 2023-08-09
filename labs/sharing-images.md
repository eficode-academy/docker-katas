# Sharing images

Before we can take our dockerized Flask app to another computer, we need to push it up to Docker Hub so that publicly avaliable.

Docker Hub is like GitHub for Docker images. It’s the main place people store their Docker images in the cloud.

> :bulb: In order for you to do this exercise, you need to have an account. If not, create an account on the Docker Hub - it's free:
> [https://hub.docker.com/signup](https://hub.docker.com/signup)

Then, login to that account by running the `docker login` command on your laptop.

> :bulb: If you do not want your password to be stored on the workstation, create an access token instead: https://hub.docker.com/settings/security

We're almost ready to push our Flask image up to the Docker Hub. We just need to rename it to our namespace (which is the same as our docker username) first.

Using the `docker tag` command, tag the image you created in the previous section to your namespace. For example, I would run:

```
docker tag myfirstapp <your-dockerhub-username>/myfirstapp:latest
```

`myfirstapp` is the tag I used in my `docker build` commands in the previous section, and `<your-dockerhub-username>/myfirstapp:` is the full name of the new Docker image I want to push to the Hub.
The `:latest` is a versioning scheme you can append to.

All that's left to do is push up your image:

```
docker push <your-dockerhub-username>/myfirstapp
```

Expected output:

```
The push refers to a repository [docker.io/<your-dockerhub-username>/myfirstapp]
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

Go to your profile page on the Docker Hub and you should see your new repository listed:
[https://hub.docker.com/repos/u/<username>](https://hub.docker.com/repos/u/<username>)

**Congrats!** You just made your first Docker image and shared it with the world!

Try to pull down the images that your fellows have pushed to see that it's really up there.

For more info on the Docker Hub, and the cli integration,
head over to [https://docs.docker.com/docker-hub/](https://docs.docker.com/docker-hub/) and read the guides there.
