# Docker image security


## 1. Create a new image

Pull the latest alpine docker image as a base:

    $ docker pull alpine:latest

You can find out the Repository Digest for the image with this command:

    $ docker image ls --digests alpine
    REPOSITORY          TAG                 DIGEST                                                                    IMAGE ID            CREATED             SIZE
    alpine              latest              sha256:621c2f39f8133acb8e64023a94dbdf0d5ca81896102b9e57c0dc184cadaf5528   196d12cf6ab1        3 weeks ago         4.41MB

Create a simple Dockerfile image to build upon the fixed

    mkdir myalpine
    cd myalpine
    repodigest=$(docker image ls --digests alpine --format "{{.Digest}}")
    cat <<EOF > Dockerfile
    FROM alpine@${repodigest}
    MAINTAINER some maintainer <maintainer@example.com>

    EXPOSE 443
    EOF

Perform a build on this image:

    docker build -t myalpine:1.0 .

### Checking the digests

At this stage you will note that the image does not have a digest:

    docker image ls --digests myalpine
    REPOSITORY          TAG                 DIGEST              IMAGE ID            CREATED             SIZE
    myalpine            1.0                 <none>              85aed7cdf75d        38 minutes ago      4.41MB

This is because a digest is a sha of the registry manifest and the layers.  This does not exist until the image is pushed to a registry.

Tag your image with a pushable-name (i.e. starting with your dockerhub username) and push it to docker hub.  You should be able to then see the image has a digest:

    docker image ls --digests meekrosoft/myalpine
    REPOSITORY            TAG                 DIGEST                                                                    IMAGE ID            CREATED             SIZE
    meekrosoft/myalpine   1.0                 sha256:b609be091e06834208b9d1d39e7e0fbfd60b550ea5d43a5476241d6218a8ee96   85aed7cdf75d        41 minutes ago      4.41MB

Ask your neighbour to pull your image and check the digest.

Fint!
