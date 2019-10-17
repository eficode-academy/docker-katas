# Learn how to join containers to network and process namespace of other containers
In this exercise, you will learn how to trouble-shoot and extract certain information from containers which are very limited in nature. Nginx and MySQL are good examples. They do not have much tools installed in them, and executing simple commands such as `ping`, `curl` or even `ps` takes a lot of effort. 

## Run a mysql container on default network:
```
$ docker run --name mysql -e MYSQL_ROOT_PASSWORD=secret -d mysql

```

Note:  This container runs on the default docker bridge network, so most of the things you already explored in previous exercises.

### Things you should explore:
* Can you `exec` into the container, and run these commands? `ifconfig`, `ip`, `ping`, `curl`, `ps`, `netstat`, `top`
* If not, what are your options? Would you choose to install these tools from various OS pacakges inside the container?

## Run a **multitool** container and make it join the network of mysql container:
```
$ docker run --name multitool --network container:mysql \
  --rm -it praqma/network-multitool /bin/bash
```


### Things you should explore:
* Exec into the multitool container and see what is the IP address of this container?
* Exec into the multitool container and see what is the IP address of mysql container?
* Is there a corresponding veth interface on the host computer for this container?
* Is there a corresponding veth interface on the host computer for mysql container?
* Explore IP address, MAC, DNS , settings for this container.
* Explore IP address, MAC, DNS , settings for mysql container.
* What are similarities and differences between the network settings of the mysql container and the multitool container?
* Inspect docker network and mysql container - from the host.
* Can you see what processes are running in multitool container?
* Can you see what processes are running in mysql container?

## Run a multitool (busybox) container and make it join the process namespace of the mysql container:
```
$ docker run --name busybox --pid container:mysql \
  --rm -it busybox /bin/sh
```

### Things you should explore:
* Exec into the multitool container and see what is the IP address of this container?
* Exec into the multitool container and see what is the IP address of mysql container?
* Is there a corresponding veth interface on the host computer for this container?
* Is there a corresponding veth interface on the host computer for mysql container?
* Explore IP address, MAC, DNS , settings for this container.
* Explore IP address, MAC, DNS , settings for mysql container.
* What are similarities and differences between the network settings of the mysql container and the multitool container?
* Inspect docker network and mysql container - from the host.
* Can you see what processes are running in multitool container?
* Can you see what processes are running in mysql container?

## Run a multitool (busybox) container and make it join the network **and** process namespace of the mysql container:
```
$ docker run --name busybox \
  --network container:mysql \
  --pid container:mysql \
  --rm -it busybox /bin/sh
```

### Things you should explore:
* Exec into the multitool container and see what is the IP address of this container?
* Exec into the multitool container and see what is the IP address of mysql container?
* Is there a corresponding veth interface on the host computer for this container?
* Is there a corresponding veth interface on the host computer for mysql container?
* Explore IP address, MAC, DNS , settings for this container.
* Explore IP address, MAC, DNS , settings for mysql container.
* What are similarities and differences between the network settings of the mysql container and the multitool container?
* Inspect docker network and mysql container - from the host.
* Can you see what processes are running in multitool container?
* Can you see what processes are running in mysql container?

# Useful commands:
* docker ps
* docker ls
* docker network ls
* docker inspect
* docker exec -it <container name|id> </some/shell>
* ip addr show
* netstat
* ps aux
* iptables -L 
* iptables -t nat -L
* iptables-save (This will not *save* any rules. It will just list them on the screen.)

