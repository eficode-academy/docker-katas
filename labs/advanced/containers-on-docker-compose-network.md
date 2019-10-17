# Exploration - Containers on docker-compose bridge network
In this exercise, you will explore various characterstics of the docker-compose network bridge, and the containers running in that network. 

Spoiler: Network created by `docker-compose` are same as user-defined networks. These networks and the containers running in these networks exihibit similar behavior.

## Create/run a multi-container `docker-compose.yml` application:
```
$ vi docker-compose.yml 
version: "3"
services:
  apache:
    image: httpd:alpine
  postgres:
    image: postgres
    environment:
      - POSTGRES_PASSWORD=secret

$ docker-compose up -d

$ docker-compose ps
```

Note: `praqma/network-multitool` is a small image with lots of network troubleshooting tools installed in it. It also runs a nginx web server on port 80 by default. Most of our examples will use this image in the *web* role. Just think of it as nginx image, with some extra bells and whistles. You can  replace Apache `httpd:alpine` image with `praqma/network-multitool` image in the above example, if you want to.

## You should investigate:
* What docker networks exist on the host network? (`docker network ls`)
* See what network interfaces exist on the host network? Do you see docker0?
* Do you see any other/additional network interface? (e.g br-123zbc456xyz)
* Inpsect docker0 bridge.
* Inpsect the newly created docker-compose bridge (e.g br-123zbc456xyz).
* What does the docker-compose network (bridge) interface on the host look like? (IP/network address, NetMask, MAC address, etc?)
* What are the IP addresses of the containers?
* What DNS resolver do these containers use? 
* Can the containers on the docker-compose bridge network access each other by their names? IP addresses?
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
* iptables -L 
* iptables -t nat -L
* iptables-save (This will not *save* any rules. It will just list them on the screen.)
