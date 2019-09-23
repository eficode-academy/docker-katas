# Docker labs

In this folder are a lot of exercises. They are numbered in the way we think makes sence to introduce the concepts.

Below is a cheatsheet for many of the commands we will touch uppon in the lab.

```bash
docker build -t friendlyname .              # Create image using this directory's Dockerfile

docker container run -p 4000:80 friendlyname    # Run "friendlyname" mapping port 4000 to 80

docker container run -d -p 4000:80 friendlyname           # Same thing, but in detached mode

docker container run -ti friendlyname               # Run "friendlyname" in interactive mode

docker container ls                                            # List all running containers

docker container ls -a                         # List all containers, even those not running

docker container exec -it <hash> bash           # Interacts with container and executes bash

docker container stop <hash>                       # Gracefully stop the specified container

docker container kill <hash>                     # Force shutdown of the specified container

docker container rm <hash>                    # Remove specified container from this machine

docker container prune                                       # Remove all stopped containers

docker volume create <name>                 # Creates a named volume with the default driver

docker volume inspect <name>              # prints out details about the given volume entity

docker volume rm <name>                           # removes the given volume from the system

docker image ls -a                                         # List all images on this machine

docker image rm <image id>                        # Remove specified image from this machine

docker image prune                          # Remove all 'dangling' images from this machine

docker image prune -a  # Remove all images without at least one container associated to them

docker system prune # delete all unused data; containers, volumes and images w.o. containers

docker system df -v       # presents a summary of the space used by different docker objects

docker login                         # Log in this CLI session using your Docker credentials

docker tag <image> username/repository:tag              # Tag <image> for upload to registry

docker push username/repository:tag                        # Upload tagged image to registry

docker run username/repository:tag                               # Run image from a registry

Ctrl + P, Ctrl + Q                    # Detach from container you're in, but keep it running

Ctrl + D                        # Detach from container you're in, and stop it, same as exit
```
