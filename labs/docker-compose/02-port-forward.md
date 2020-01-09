# A basic webserver

Running arbitrary Linux commands inside a Docker container with docker-compose is quite overhead, but let's do something more useful.

Create docker-compose.yml file with following content:
```yml
version: '3'
services:
    nginx:
        image: nginx:latest
```

Now run the command `docker-compose pull` and you should see:
```bash
Pulling nginx ... done
```
This Docker image uses the [Nginx](http://nginx.org/) webserver to serve a static HTML website.

Configure you're nginx service to expose port 80 as 8080.
With: 
```yml
        ports: 
          - "8080:80"
```

Where first one is HOST and second one is CONTAINER.


Open a web browser and go to port 8080 on your host. The exact address will depend on how you're running Docker today:

* **Native Linux** - [http://localhost:8080](http://localhost:8080)
* **Cloud server** - Make sure firewall rules are configured to allow traffic on port 8080. Open browser and use the hostname (or IP) for your server.
Ex: [http://ec2-54-69-126-146.us-west-2.compute.amazonaws.com:8080](http://ec2-54-69-126-146.us-west-2.compute.amazonaws.com:8080) -
Alternatively open a new shell and issue `curl localhost:8080`

If you see a webpage saying "Welcome to nginx!" then you're done!

If you look at the console output from docker, you see nginx producing a line of text for each time a browser hits the webpage:

```bash
$ docker-compose up
Creating docker-compose_nginx_1 ... done
Attaching to docker-compose_nginx_1
nginx_1  | 172.18.0.1 - - [07/Jan/2020:22:28:48 +0000] "GET / HTTP/1.1" 200 612 "-" "curl/7.58.0" "-"
```

Press **control + c** in your terminal window to stop your services.

## Working with your docker container

When running a webserver like nginx, it's pretty useful that you do not have to have an open session into the server at all times to run it.
We need to make it run in the background, freeing up our terminal for other things.
Docker enables this with the `-d` parameter for run.
`docker-compose up -d`

```bash
$ docker-compose up -d
Starting docker-compose_nginx_1 ... done
```

Docker prints out the service name and returns to the terminal.

To see what services you've running from compose file you can use `docker-compose ps`

_*Q: What is the status?*_

To stop services you can run `docker-compose down`

_*Q: What is the status after stopping?*_
