# Docker networks

When you spin up several containers, they all share the same internal network.

In this exercise, we all use this for creating a `nginx` server that serves content from two other containers. For simplicity, the same `nginx` container wil be used, but each serving a page with a different background color, so it's easier to see that it behaves correctly.

## Creating the containers

There's a `docker-compose.yml` in this folder that contains the necessary configuration to spin up three nginx containers, configured to be a master nginx container. Then do a `docker-compose up -d`. When you open a browser window, go to http://localhost:9090/ You'll see the default page from a new `nginx` installation. The configuration file here makes two entry points for the two containers. In the configuration, the host names are used, instead of IP addresses, Docker assigns IP addresses to containers, but the values can vary between runs. If you inspect the docker containers, you'll see a section with `Networks`, where the alias is listed . When docker containers are created, a host name is assigned to them, which is by default the same as the container name. In the `nginx` configuration file, the host names are used without the ports defined in the above example; this is because docker has an internal network, and the `nginx` image by default exposes port 80.

## Running the containers

Start the containers

    $ docker-compose up -d

The port numbers that are exposed were chosen a bit arbitrarily. You can remove the port configuration from container `one` and `two`, and see if the setup still works.
