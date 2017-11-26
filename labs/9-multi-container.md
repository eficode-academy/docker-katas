# Docker compose

If you have started working with Docker and are building container images for your application services, you most likely have noticed that after a while you may end up writing long `docker container run` commands.
These commands, while very intuitive, can become cumbersome to write, especially if you are developing a multi-container applications and spinning up containers quickly.
[Docker Compose](https://docs.docker.com/compose/install/) is a “*tool for defining and running your multi-container Docker applications*”.

Your applications can be defined in a YAML file where all the options that you used in `docker container run` are defined.
Compose also allows you to manage your application as a single entity rather than dealing with individual containers.

This file defines all of the containers and settings you need to launch your set of clusters. The properties map onto how you use the docker run commands, however, are now stored in source control and shared along with your code.

## Terminology

- *docker-compose.yml* The YAML file where all your configuration of your docker containers go.
- *docker-compose* The cli tool that enables you to define and run multi-container applications with Docker
  - *run* :
  - *up* : creates and starts the services stated in the compose file
  - *down* : stops and removes containers, networks, images, and volumes
  - *restart* :
  - *logs* : streams the acummulated logs from all the containers in the compose file
  - *ps* : same as pure `docker` variant; shows you all containers that are currently running.
  - *rm* : removes all the containers from the given compose file.
  - *start* : starts the services
  - *stop* : stops the services

The docker cli is used when managing individual containers on a docker engine.
It is the client command line to access the docker daemon api.

The docker-compose cli can be used to manage a multi-container application.

## Task: host a Wordpress site

In this scenario, we are going to deploy the CMS system called Wordpress.

> WordPress is a free and open source blogging tool and a content management system (CMS) based on PHP and MySQL, which runs on a web hosting service.

So we need two containers:

- One container that can serve the Wordpress PHP files
- One container that can serve as a MySQL database for Wordpress.

Both containers already exists on the dockerhub: [Wordpress](https://hub.docker.com/_/wordpress/) and [Mysql](https://hub.docker.com/_/mysql/).

## Separate containers

`docker container run --name mysql-container --rm -p 3306:3306 -e MYSQL_ROOT_PASSWORD=wordpress -d mysql`

> The `-e` option is used to inject environment variables into the container.

MySQL is now exposing it's port 3306 on the host, and everybody can attach to it **so do not do this in production without firewall**.

We need to connect our wordpress container to the host's IP address.
You can get the IP by issuing the `ifconfig` command on the host.

After you have noted down the IP, spin up the wordpress container with the host IP as a variable:

`docker container run -e WORDPRESS_DB_HOST=<YOURIP> -e WORDPRESS_DB_PASSWORD=wordpress --name wordpress-container --rm -p 8080:80 -d wordpress`

You can now browse to the IP:8080 and have your very own wordpress server running.

## Docker Link Flag

Even though we in two commands made the setup running in the above scenarios, there is some problems here we can fix: We need to know the host IP, and we have exposed the database to the outside world.

In order to connect together multiple docker containers or services running inside docker container, `–link` flag can be used in order to securely connect and provide a channel to transfer information from one container to another. 

As, we have linked both the container now wordpress container can be accessed from browser using the address http://localhost:8080 and setup of wordpress can be done easily.

`docker run --link mysql-container:mysql --name wordpress-container --rm -p 8080:80 wordpress`

## Using docker-compose
