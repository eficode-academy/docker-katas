# Exploration - Containers on user-defined bridge network
In this exercise, you will explore various characterstics of the user-defined network bridge, and the containers running in that network.

## Create a user-defined docker network bridge:
```
docker network create mynet
```

## You should investigate:
* What docker networks exist on the host network? (`docker network ls`)
* See what network interfaces exist on the host network? Do you see docker0?
* Do you see any other/additional network interface? (e.g br-123zbc456xyz)
* Inspect docker0 bridge.
* Inspect the newly created bridge `mynet`.
* What does the new network (bridge) interface on the host look like? (IP/network address, NetMask, MAC address, etc?)

## Run couple of docker containers on the user-defined bridge network

Note: `praqma/network-multitool` is a small image with lots of network troubleshooting tools installed in it. It also runs a nginx web server on port 80 by default. Most of our examples will use this image in the *web* role. Just think of it as nginx image, with some extra bells and whistles.

Note: You may want to stop/remove container you created in the previous exercises before running the new ones shown below.

```
$ docker run --name=web --network=mynet \
-d praqma/network-multitool

$ docker run --name=db --network=mynet \
-e MYSQL_ROOT_PASSWORD=secret \
-d mysql
```

### You should investigate:
* What are the IP addresses of the containers?
* What DNS resolver do these containers use?
* Can the containers on the user-defined bridge network access each other by their names? IP addresses?
* Do you see any **veth** interfaces on the host?
* Compare MAC addresses of veth interfaces on the host and the **eth0** interfaces on each container.
* Inspect containers for their IP addresses, MAC addresses, etc.
* Explore what processes are listening on various network interfaces on the host.
* Explore what processes are listening on various network interfaces on the container.

## Explore IPTables magic happening inside the containers:
* Run another container on the user-defined network, with special privileges, and use that to explore the IPTables rules setup on the container.
* Optionally, explore the iptables rules on the host as well.

```
$ docker run --cap-add=NET_ADMIN --cap-add=NET_RAW \
  --name multitool --network mynet \
  -it praqma/network-multitool /bin/bash

$ iptables-save

$ netstat -ntlp
```



# Useful commands:
* docker ps
* docker ls
* docker network ls
* docker inspect
* docker exec -it <container name|id> </some/shell>
* ip addr show
* netstat
* iptables -L
* iptables -t nat -L
* iptables-save (This will not *save* any rules. It will just list them on the screen.)
