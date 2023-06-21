# A basic webserver

Running arbitrary Linux commands inside a Docker container is fun, but let's do something more useful.

Pull down the `nginx` Docker image from the Docker Hub. This Docker image uses the [Nginx](http://nginx.org/) webserver to serve a static HTML website.

Start a new container from the `nginx` image that exposes port 80 from the container to port 8080 on your host. You will need to use the `-p` flag with the docker container run command.

> :bulb: Mapping ports between your host machine and your containers can get confusing.
> Here is the syntax you will use:
>
> ```
> docker run -p 8080:80 nginx
> ```
>
> The trick is to remember that **the host port always goes to the left**,
> and **the container port always goes to the right.**
> Remember it as traffic coming _from_ the host, _to_ the container.

Open a web browser and go to port 8080 on your host. The exact address will depend on how you're running Docker today:

- **Native Linux** - [http://localhost:8080](http://localhost:8080)
- **Cloud server** - Make sure firewall rules are configured to allow traffic on port 8080. Open browser and use the hostname (or IP) for your server.
  Ex: [http://inst1.prefix.eficode.academy:8080](http://inst1.prefix.eficode.academy:8080) -
  Alternatively open a new shell and issue `curl localhost:8080`
- **Google Cloud Shell** - Open Web Preview (upper right corner)

If you see a webpage saying "Welcome to nginx!" then you're done!

If you look at the console output from docker, you see nginx producing a line of text for each time a browser hits the webpage:

```
docker run -p 8080:80 nginx
```

Expected output:

```
172.17.0.1 - - [31/May/2017:11:52:48 +0000] "GET / HTTP/1.1" 200 612 "-" "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:53.0) Gecko/20100101 Firefox/53.0" "-
```

Press **control + c** in your terminal window to stop your container.

## Running the webserver container in the background

When running a webserver like nginx, it is very useful to not run the container in the foreground of our terminal.
Instead we should make it run in the background, freeing up our terminal for other things.
Docker enables this with the `-d` parameter for the `run` command.
For example: `docker run -d -p 8080:80 nginx`

```
docker run -p 8080:80 -d nginx
```

Docker prints out the container ID and returns to the terminal.

Congratulations! You have just started a container in the background. :tada:

## Cleanup

Stop the container you just started.
Remember that your container ID is different from the one in the example.

```bash
docker stop 78c943461b49584ebdf841f36d113567540ae460387bbd7b2f885343e7ad7554
```
Docker prints out the ID of the stopped container.

```
78c943461b49584ebdf841f36d113567540ae460387bbd7b2f885343e7ad7554
```
