# Exploration - Containers on none network
In this exercise, you will explore various characterstics of the **none** network, and the containers running in that network. 

## Run a container in "none network" mode:
```
docker run --name multitool --network none -p 80:80 -d praqma/network-multitool
```

Optionally, run another container in the  "none network" mode.
```
docker run --name mysql --network none -e MYSQL_ROOT_PASSWORD=secret -d  mysql
```

 

## You should investigate:
* What docker networks exist on the host? (`docker network ls`)
* See what network interfaces exist on the host network? Do you see docker0?
* Do you see any new network interface(s)? (e.g br-123zbc456xyz)
* Do you see any new **veth** interfaces on the host?
* Do you see an **eth0** interface inside the container?
* What is the IP address of the container(s)?
* What DNS resolver do this container(s) use? 
* Can a container on this network reach/access any other containers by their names? IP addresses?
* Inspect container(s) for their IP addresses, MAC addresses, etc.
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
* ps aux
* iptables -L 
* iptables -t nat -L
* iptables-save (This will not *save* any rules. It will just list them on the screen.)
