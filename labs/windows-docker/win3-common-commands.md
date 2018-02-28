# The usual set of Docker commands on Windows

Now, time to show that Docker on Windows really just is Docker as you know it from Linux by now. 

## Volumes

Let's start with volume. Make a folder in your C drive, called data and run:  

```
mkdir \data

docker container run -it -v C:\data:C:\data microsoft/nanoserver powershell
```

Make some files in the directory by your nomal explorer, and run the following in your container:
```
PS C:\> dir data


    Directory: C:\data


Mode                LastWriteTime         Length Name
----                -------------         ------ ----
-a----       11/29/2017  12:54 PM              0 Docker.txt
-a----       11/29/2017  12:54 PM              0 On.txt
-a----       11/29/2017  12:54 PM              0 Windows.txt
```

## Building 

Similarly, let's play with a Dockerfile like the one below:
```
FROM microsoft/windowsservercore

ENV chocolateyUseWindowsCompression false

RUN powershell -Command \
    iex ((new-object net.webclient).DownloadString('https://chocolatey.org/install.ps1'));
```
`Build` that image, and run a container based on your image.

You now have chocolatey (a package manager for Windows powershell): 
```
chocolatey -?
``` 
It will print out the avaliable commands on chocolatey. Now, exit the container again.

## Port forwarding

Let's look at an IIS Server: 
```
docker run -d -p 80:80 --name iis microsoft/iis
```

Which can be accessed via your windows machine on this ip: 
```
docker inspect --format '{{ .NetworkSettings.Networks.nat.IPAddress }}' iis
```
You see the welcome screen of the IIS, but that is not very usefull, so let's start making an application.

## Compiling and serving code

This fresh installation does not have golang installed. We can just use a container to fix that. 
Create the following file called `webserver.go`: 

```
package main

import (
    "fmt"
    "net/http"
    "os"
)

func main() {
    port := os.Getenv("PORT")
    if port == "" {
        port = "8080"
    }
    fmt.Println("Proudly serving content on port", port)
    panic(http.ListenAndServe(fmt.Sprintf(":%s", port), http.FileServer(http.Dir("."))))
}
```
and run: 

```
docker run -it -v C:\data:C:\code -w C:\code golang:nanoserver powershell

go build webserver.go
```

Voila. Webserver.exe has been put into the current directory. 

Now we need to serve the application in a container.

Make the following dockerfile in the same directory: 
```
FROM microsoft/nanoserver

COPY webserver.exe /webserver.exe

EXPOSE 8080

CMD ["/webserver.exe"]
```
And build an image.

IIS needs to be able to find it later, and it does not run on localhost. So we need to name our container: 
```
docker run -d --rm --name=mysite -p 8080:8080 <yourtag>
```

> the `--rm` part makes sure that if the container stops, it gets automatically deleted.

You can access it by running: 
```
start http://$(docker inspect -f '{{ .NetworkSettings.Networks.nat.IPAddress }}' mysite):8080
```

It will show you a folder view of the containers files, including the webserver.exe that is running.

## Summary

This concludes the Windows bit of the workshop for now, but everything you worked with in regards to Docker works with Windows. 

However multi container builds require a newer version of Docker than the Virtual machines have, so this is something you'll have to try at home ;) 
