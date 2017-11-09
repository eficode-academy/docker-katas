# Throw your container away

As containers are just a thin base layer on top of the host kernel, it is really fast to spin up a new instance if you crashed your old one.

Let's try to run an alpine container and delete the file system.

Spin up the container with `docker run -ti alpine`

list all the folders on the root level to see the whole distribution:
```
# ls /

bin    etc    lib    mnt    root   sbin   sys    usr
dev    home   media  proc   run    srv    tmp    var
```

Now, delete the whole file system with `rm -rf /`

> **Warning:** Do never try to run this command as a super user in your own environment, as it will delete *everything* on your computer.

Try to navigate around to see how much of the OS is gone

```
# ls
/bin/sh: ls: not found

# whoami
sh: whoami: not found

# date
/bin/sh: date: not found
```

Exit out by `Ctrl+D` and create a new instance of the Alpine image

```
$ docker run -ti alpine
# ls /
bin    etc    lib    mnt    root   sbin   sys    usr
dev    home   media  proc   run    srv    tmp    var
```

Try to perform the same tasks as displayed above to see that you have a fresh new instance ready to go.
