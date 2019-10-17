# Exploration - Containers on default bridge network
In this exercise, you will explore various characterstics of the default network bridge, and the containers running in that network. 


## You should investigate:
* What docker networks exist on the host network? (`docker network ls`)
* See what network interfaces exist on the host network? Do you see docker0?
* Inpsect docker0 bridge.
* What does docker0 network interface on the host look like? (IP/network address, NetMask, MAC address, etc?)

## Run couple of docker containers on the bridge network

Note: `praqma/network-multitool` is a small image with lots of network troubleshooting tools installed in it. It also runs a nginx web server on port 80 by default. Most of our examples will use this image in the *web* role. Just think of it as nginx image, with some extra bells and whistles.

```
$ docker run --name web \
  -d praqma/network-multitool

$ docker run --name db \
  -e MYSQL_ROOT_PASSWORD=secret \
  -d mysql
```

### You should investigate:
* What are the IP addresses of the containers?
* What DNS resolver do these containers use? 
* Can the containers on the default bridge network access each other by their names? IP addresses?
* Do you see any **veth** interfaces on the host?
* Compare MAC addresses of veth interfaces on the host and the **eth0** interfaces on each container.
* Inspect containers for their IP addresses, MAC addresses, etc.
* Explore what processes are listening on various network interfaces on the host.
* Explore what processes are listening on various network interfaces on the container.


# Useful commands:
* docker ps
* docker ls
* docker network ls
* docker inspect
* docker exec -it <container name|id> </some/shell>
* ip addr show
* netstat
