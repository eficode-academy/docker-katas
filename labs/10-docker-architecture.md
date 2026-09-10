# Docker architecture deep dive

## Learning Goals

- Understand the client-server architecture of Docker (client, daemon, containerd, runc)
- Understand how images are built from layers, and how the union filesystem works
- Understand what a storage driver is, and how copy-on-write works for a running container
- Understand the difference between the default bridge and a user-defined bridge network
- Understand `host` and `none` networking, and how published ports (`-p`) work

## Introduction

So far you have used the `docker` command as a single tool, but under the hood Docker is actually made up of several cooperating components:

```
- docker CLI client
- dockerd - the Docker daemon
- containerd
- runc
```

- **docker CLI** - the client you type commands into. It only knows how to talk to the API, it does not run anything itself.
- **dockerd** - the daemon. It manages images, containers, networks and volumes, and exposes the Docker REST API.
- **containerd** - a smaller daemon that dockerd delegates the actual container lifecycle (start/stop/pause) to.
- **runc** - the low-level tool that actually creates a container using Linux kernel primitives: `namespaces` (isolation) and `cgroups` (resource limits).

None of this is Docker "magic" - it is just Linux features, wrapped in a convenient API and CLI.

## Exercise

### Overview

- Inspect the client/server split and the daemon's configuration
- Explore how an image is made up of layers
- Explore the storage driver and copy-on-write behaviour of a running container
- Compare the default bridge and a user-defined bridge network
- Try `host` and `none` networking, and see how published ports (`-p`) actually work

### Step by step instructions

<details>
<summary>Inspect the client/server split and the daemon's configuration</summary>

- Run `docker version`. Notice the output is split into a `Client` and a `Server` section - they can even be different versions, since they are two separate programs talking over an API.
- Run `docker info`. Among a lot of useful information, look for:
  - `Server Version`
  - `Storage Driver`
  - `Cgroup Driver` / `Cgroup Version`
  - `containerd version`
  - `runc version`

> :bulb: `docker info` is a great first command to run when troubleshooting a Docker host, it gives you a full health-check of the daemon.

</details>

<details>
<summary>Explore how an image is made up of layers</summary>

Every instruction in a Dockerfile that changes the filesystem (`RUN`, `COPY`, `ADD`) creates a new, read-only **layer**. Layers are stacked on top of each other with a **union filesystem**, so the container sees one merged filesystem, even though it is really built from many layers.

- Reuse the Dockerfile from [07-building-an-image](07-building-an-image.md) (in [building-an-image](building-an-image/)), or pick any image you already built earlier in the labs.
- Run `docker history <image-name>` on it. Each row is a layer, showing the command that created it and the size it added.
- Run `docker inspect <image-name>` and look at the `RootFS.Layers` array - this lists the actual content-addressable layer IDs that make up the image.

> :bulb: This is why the order of instructions in a Dockerfile matters for build speed: Docker caches each layer, and only rebuilds a layer (and everything after it) if the instruction or its input files changed. This is also why you COPY dependency manifests (like `requirements.txt`) and install dependencies *before* copying your application code - your code changes far more often than your dependencies.

- Try adding an extra `RUN` instruction to the end of your Dockerfile (e.g. `RUN echo "layer test"`) and rebuild. Run `docker history` again - notice only one new layer was added, the rest were reused (`CACHED`).

</details>

<details>
<summary>Explore the storage driver and copy-on-write</summary>

Images are made of read-only layers. When you start a container, Docker adds one thin **writable layer** on top - this is where all the changes made by the running container are stored. This mechanism is called **copy-on-write (CoW)**: if a container wants to modify a file that lives in a read-only layer below, the storage driver copies that file up into the writable layer first, then modifies the copy.

- Check which storage driver your host uses: `docker info | grep -i "storage driver"` (this is very likely to be `overlay2`, the modern default on Linux).
- Start a container and make a change to its filesystem, then compare it against the image:

  ```bash
  docker run -d --name diff-demo ubuntu sleep 300

  docker exec diff-demo touch /tmp/new-file.txt

  docker exec diff-demo sh -c "echo hello >> /etc/hostname"

  docker diff diff-demo
  ```

  `docker diff` shows you exactly what changed in the container's writable layer compared to the image it was started from: `A` for added, `C` for changed, `D` for deleted.

- Clean up: `docker stop diff-demo && docker rm diff-demo`

> :bulb: If you have root/sudo access to the Docker host's filesystem, you can look directly at the layers on disk under `/var/lib/docker/overlay2/`. Each subdirectory is one layer, and you'll find a `diff` folder in it containing exactly the files that layer contributes.

- This is also why containers are considered **ephemeral**: the writable layer is deleted along with the container. Anything you want to keep must live in a [volume](06-volumes.md) instead.

</details>

<details>
<summary>Bridge networks: default vs. user-defined</summary>

Every container connects to a network when it starts. Docker ships a **default bridge network** (called `bridge`, backed by a host interface called `docker0`), but the official recommendation is to always create your own **user-defined bridge network** instead - here's the one behavior difference that matters most.

- `docker network ls` - you'll always see `bridge`, `host` and `none`; these three exist on every Docker host.
- Start two containers *without* specifying `--network`, so they land on the default `bridge`:

  ```bash
  docker run -dit --name alpine1 alpine ash
  docker run -dit --name alpine2 alpine ash
  ```

- `docker network inspect bridge` - both containers are listed, each with its own IP (e.g. `172.17.0.2`, `172.17.0.3`).
- From `alpine1`, ping the other container **by IP** - works: `docker exec alpine1 ping -c2 172.17.0.3`
- From `alpine1`, ping the other container **by name** - fails: `docker exec alpine1 ping -c2 alpine2` (`bad address 'alpine2'`)
- Clean up, then repeat on a network you create yourself:

  ```bash
  docker rm -f alpine1 alpine2

  docker network create alpine-net

  docker run -dit --name alpine1 --network alpine-net alpine ash
  docker run -dit --name alpine2 --network alpine-net alpine ash
  ```

- Repeat the ping by name: `docker exec alpine1 ping -c2 alpine2` - this time it **works**. User-defined networks give you Docker's embedded DNS server for free; the default bridge doesn't.

> :bulb: This is exactly why [09-multi-container](09-multi-container.md) and [11-security-networking](11-security-networking.md) always create a dedicated network for services to talk over. See the [bridge network driver docs](https://docs.docker.com/engine/network/drivers/bridge/) for the full list of differences.

#### Clean up

```bash
docker rm -f alpine1 alpine2
docker network rm alpine-net
```

</details>

<details>
<summary>Host and none networking, and how published ports work</summary>

Besides bridge networks, Docker ships two special-purpose modes, plus one mechanism you've been using all along without seeing how it works.

**`--network host`** - the container shares the host's network stack directly; it gets no IP address of its own.

```bash
docker run -d --rm --network host --name web python:3-alpine python3 -m http.server 8888
```

- `docker exec web ip addr show` - the **host's real interfaces**, not an isolated container `eth0`.
- `docker port web` - prints nothing (or errors), since host-mode containers don't have a port mapping to report.
- `curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8888` - reaches the server directly, even though you never published a port with `-p`.
- Because there's no container IP to map to, published ports (`-p`) are **ignored** in this mode - Docker even prints a warning if you try: `WARNING: Published ports are discarded when using host network mode`.

**`--network none`** - the opposite extreme: only the loopback interface, no connectivity at all.

```bash
docker run -d --rm --network none --name db -e MYSQL_ROOT_PASSWORD=secret mysql
```

- `docker exec db ip addr show` - only `lo`, no `eth0`.
- `docker exec db ping -c1 8.8.8.8` - fails, there is no route out of the container.

**How does `-p` work on a normal bridge network, then?** Docker adds a firewall (`iptables`/`nftables`) rule that forwards traffic arriving on the host port to the container's private IP and port - this is what makes `docker run -p 8080:80 nginx` reachable at `http://localhost:8080`.

```bash
docker run -d --rm -p 8080:80 --name web2 nginx
sudo iptables -t nat -L DOCKER -n     # look for a DNAT rule: tcp dpt:8080 -> <container IP>:80
```

> :bulb: `none` is a good default for batch/offline jobs that shouldn't have any network access by design - see [11-security-networking](11-security-networking.md) for minimizing a container's attack surface more generally. See the [port publishing docs](https://docs.docker.com/engine/network/port-publishing/) for the full details on how Docker maps ports.

#### Clean up

```bash
docker rm -f web db web2
```

</details>

### Clean up

```bash
docker rm -f diff-demo alpine1 alpine2 web db web2 2>/dev/null
docker network rm alpine-net 2>/dev/null
```
