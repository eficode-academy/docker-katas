# Docker volumes

In some cases you're going to need data outside of docker containers and to do that you can use volumes

**A docker Volume** is where you can use a named or unnamed volume to store the external data. You would normally use a volume driver for this, but you can get a host mounted path using the default local volume driver.

So let's look at the [Nginx](https://hub.docker.com/_/nginx/) service from port-forwarding excercise.
The server itself is of little use, if it cannot access our web content on the host.

We can define volumes with docker-compose for the service like this:

```yml
version: '3'
services:
    nginx:
        image: nginx:latest
        volumes:
            - "./web_content:/usr/share/nginx/html:ro"
```

Where web_content folder is mapped to nginx html folder. `:ro` defines that it's read only.

Try to do the following:

1. Create folder `web_content`
2. Create index.html file under `web_content`
3. Modify html file
4. Restart the nginx service with compose

_*Q: What do you see now in the browser?*_

Now modify the index.html again and *do* not restart the container.

_*Q: What do you see after you refresh the browser page?*_


