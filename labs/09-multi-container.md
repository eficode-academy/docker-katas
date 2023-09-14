# Multi container setups

## Task: host a Wordpress site

In this scenario, we are going to deploy the CMS system called Wordpress.

> WordPress is a free and open source blogging tool and a content management system (CMS) based on PHP and MySQL, which runs on a web hosting service.

So we need two containers:

- One container that can serve the Wordpress PHP files
- One container that can serve as a MySQL database for Wordpress.

Both containers already exists on the dockerhub: [Wordpress](https://hub.docker.com/_/wordpress/) and [Mysql](https://hub.docker.com/_/mysql/).

## Separate containers

To start a mysql container, issue the following command

```bash
docker run --name mysql-container --rm -p 3306:3306 -e MYSQL_ROOT_PASSWORD=wordpress -e MYSQL_DATABASE=wordpressdb -d mysql:5.7.36
```

Let's recap what this command does:

- `docker` invokes the docker engine
- `run` tells docker to run a new container off an image
- `--name mysql-container` gives the new container a name for better referencing
- `--rm` tells docker to remove the container after it is stopped
- `-p 3306:3306` mounts the host port 3306, to the containers port 3306.
- `-e MYSQL_ROOT_PASSWORD=wordpress` The `-e` option is used to inject environment variables into the container.
- `-e MYSQL_DATABASE=wordpressdb` denotes the name of the database created when mysql starts up.
- `-d` runs the container detached, in the background.
- `mysql` tells what container to actually run, here mysql:latest (:latest is the default if nothing else is specified)

MySQL is now exposing it's port 3306 on the host, and everybody can attach to it **so do not do this in production without proper security settings**.

We need to connect our wordpress container to the host's IP address.
You can either use the external IP address of your server, or the DNS name if you are at a training, e.g. `workstation-<num>.<prefix>.eficode.academy`.

After you have noted down the IP, spin up the wordpress container with the host IP as a variable:

```bash
docker run --name wordpress-container --rm -e WORDPRESS_DB_HOST=172.17.0.1 -e WORDPRESS_DB_PASSWORD=wordpress -e WORDPRESS_DB_USER=root -e WORDPRESS_DB_NAME=wordpressdb -p 8080:80 -d wordpress:5.7.2-apache
```

You can now browse to the IP:8080 and have your very own wordpress server running. Since port 3306 is the default MySQL port, wordpress will try to connect on that port by itself.

- Stop the two containers again `docker stop wordpress-container mysql-container`

## Making a container network

Even though we in two commands made the setup running in the above scenario, there are some problems here we can fix:

- We need to know the host IP to get them to talk to each other.
- And we have exposed the database to the outside world.

In order to connect multiple docker containers without binding them to the hosts network interface we need to create a docker network.

The `docker network` command securely connect and provide a channel to transfer information from one container to another.

First off make a new network for the containers to communicate through:

`docker network create if_wordpress`

Docker will return the `networkID` for the newly created network. You can reference it by name as well as the ID.

Now you need to connect the two containers to the network, by adding the `--network` option:

```bash
docker run --name mysql-container --rm --network if_wordpress -e MYSQL_ROOT_PASSWORD=wordpress -e MYSQL_DATABASE=wordpressdb -d mysql:5.7.36
af38acac52301a7c9689d708e6c3255704cdffb1972bcc245d67b02840983a50

docker run --name wordpress-container --rm --network if_wordpress -e WORDPRESS_DB_HOST=mysql-container -e WORDPRESS_DB_PASSWORD=wordpress -e WORDPRESS_DB_USER=root -e WORDPRESS_DB_NAME=wordpressdb -p 8080:80 -d wordpress:5.7.2-apache
fd4fd096c064094d7758cefce41d0f1124e78b86623160466973007cf0af8556
```

Notice the `WORDPRESS_DB_HOST` env variable. When you make a container join a network, it automatically gets the container name as DNS name as well, making it super easy to make containers discover each other. The DNS name is only visible inside the Docker network, which is also true for the `IP` address (usually an address starting with `172`) that is assigned to them. If you do not expose a port for a container, the container is only visible to Docker.

You have now deployed both containers into the network. Take a deeper look into the container network by issuing: `docker network inspect if_wordpress`.

```bash
docker network inspect if_wordpress
```

Expected output:

```json
[
  {
    "Name": "if_wordpress",
    "Id": "04e073137ff8c71b9a040ba452c12517ebe5a520960a78bccb7b242b723b5a21",
    "Created": "2017-11-28T17:20:37.83042658+01:00",
    "Scope": "local",
    "Driver": "bridge",
    "EnableIPv6": false,
    "IPAM": {
      "Driver": "default",
      "Options": {},
      "Config": [
        {
          "Subnet": "172.18.0.0/16",
          "Gateway": "172.18.0.1"
        }
      ]
    },
    "Internal": false,
    "Attachable": false,
    "Ingress": false,
    "Containers": {
      "af38acac52301a7c9689d708e6c3255704cdffb1972bcc245d67b02840983a50": {
        "Name": "mysql-container",
        "EndpointID": "96b4befec46c788d1722d61664e68bfcbd29b5d484f1f004349163249d28a03d",
        "MacAddress": "02:42:ac:12:00:02",
        "IPv4Address": "172.18.0.2/16",
        "IPv6Address": ""
      },
      "fb4dad5cd82b5b40ee4f7f5f0249ff4b7b4116654bab760719261574b2478b52": {
        "Name": "wordpress-container",
        "EndpointID": "2389930f52893e03a15fdc28ce59316619cb061e716309aa11a2716ef09cde17",
        "MacAddress": "02:42:ac:12:00:03",
        "IPv4Address": "172.18.0.3/16",
        "IPv6Address": ""
      }
    },
    "Options": {},
    "Labels": {}
  }
]
```

As, we have linked both the container now wordpress container can be accessed from browser using the address [http://localhost:8080](http://localhost:8080) and setup of wordpress can be done easily. MySQL is not accessible from the outside so security is much better than before.

### Cleanup

Close both of the containers down by issuing the following command:

```bash
docker stop wordpress-container mysql-container
```

## Using Docker compose

If you have started working with Docker and are building container images for your application services, you most likely have noticed that after a while you may end up writing long `docker container run` commands.
These commands, while very intuitive, can become cumbersome to write, especially if you are developing a multi-container applications and spinning up containers quickly.

[Docker Compose](https://docs.docker.com/compose/install/) is a “_tool for defining and running your multi-container Docker applications_”.

Your applications can be defined in a YAML file where all the options that you used in `docker run` are defined.

Compose also allows you to manage your application as a single entity rather than dealing with individual containers.

This file defines all of the containers and settings you need to launch your set of clusters. The properties map onto how you use the docker run commands, however, are now stored in source control and shared along with your code.

## Terminology

- `docker-compose.yml` The YAML file where all your configuration of your docker containers go.
- `docker-compose` The cli tool that enables you to define and run multi-container applications with Docker

  - `up` : creates and starts the services stated in the compose file
  - `down` : stops and removes containers, networks, images, and volumes
  - `restart` :
  - `logs` : streams the acummulated logs from all the containers in the compose file
  - `ps` : same as `docker ps`; shows you all containers that are currently running.
  - `rm` : removes all the containers from the given compose file.
  - `start` : starts the services
  - `stop` : stops the services

The docker cli is used when managing individual containers on a docker engine.
It is the client command line to access the docker daemon api.

The docker-compose cli together with the yaml files can be used to manage a multi-container application.

## Compose-erizing your wordpress

So we want to take advantage of docker-compose to run our wordpress site.

In order to to this we need to:

1. Transform our setup into a docker-compose.yaml file
1. Invoke docker-compose and watch the magic happen!

Head over to this labs folder:

`cd labs/multi-container`

Open the file `docker.compose.yaml` with a text editor:

`nano docker-compose.yaml`

You should see something like this:

```yaml
version: "3.1"

services:
  #  wordpress_container:

  mysql_container:
    image: mysql:5.7
    ports:
      - 3306:3306
    environment:
      MYSQL_ROOT_PASSWORD: wordpress
```

This is the template we are building our compose file upon so let's drill this one down:

- `version` indicate what version of the compose syntax we are using
- `services` is the section where we put our containers
  - `wordpress_container` is the section where we define our wordpress container
  - `mysql_container` is the ditto of MySQL.

> For more information on docker-compose yaml files, head over to the [documentation](https://docs.docker.com/compose/overview/).

The `services` part is equivalent to our `docker container run` command. Likewise there is a `network` and `volumes` section for those as well corresponding to `docker network create` and `docker volume create`.

Let's look the mysql_container part together, making you able to create the other container yourself. Look at the original command we made to spin up the container:

`docker container run --name mysql-container --rm -p 3306:3306 -e MYSQL_ROOT_PASSWORD=wordpress -e MYSQL_DATABASE=wordpressdb -d mysql:5.7.36`

The command gives out following information: a `name`, a `port` mapping, two `environment` variables and the `image` we want to run.

Now look at the docker-compose example again:

- `mysql_container` defines the name of the container
- `image:wordpress` describes what image the container spins up from.
- `ports` defines a list of port mappings from host to container
- `environment` describes the `-e` variable made before in a yaml list

Instead of keeping sensitive information in the `docker-compose.yml` file, you can also use an [`.env`](https://docs.docker.com/compose/env-file/) file to keep all the environment variables. That way, it's easier to make a development environment and a production environment with the same `docker-compose.yml`.

```conf
MYSQL_ROOT_PASSWORD=wordpress
```

Try to spin up the container in detached mode:

```bash
docker-compose up -d
Creating network "multicontainer_default" with the default driver
Creating multicontainer_mysql_container_1 ...
Creating multicontainer_mysql_container_1 ... done
```

Looking at the output you can see that it made a `docker network` named `multicontainer_default` as well as the MySQL container named `multicontainer_mysql_container_1`.

Issue a `docker container ls` as well as `docker network ls` to see that both the container and network are listed.

To shut down the container and network, issue a `docker-compose down`

> **note**: The command docker-compose down removes the containers and default network.

### Creating the wordpress container

You now have all the pieces of information to make the Wordpress container. We've copied the run command from before if you can't remember it by heart:

```bash
docker run --name wordpress-container --rm --network if_wordpress -e WORDPRESS_DB_HOST=mysql-container -e WORDPRESS_DB_PASSWORD=wordpress -e WORDPRESS_DB_USER=root -e WORDPRESS_DB_NAME=wordpressdb -p 8080:80 -d wordpress:5.7.2-apache
```

You must

- uncomment the `wordpress_container` part of the services section
- map the pieces of information from the docker container run command to the yaml format.
- remove MySQL port mapping to close that from outside reach.

When you made that, run `docker-compose up -d` and access your wordpress site from [http://IP:8080](http://IP:8080)

> **Hint**: If you are stuck, look at the file docker-compose_final.yaml in the same folder.
