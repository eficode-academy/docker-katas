# The usual set of Docker commands on Windows

Now, time to show that Docker on Windows really just is Docker as you know it from Linux by now. 

Let's start with volume. Make a folder in your C drive, called data and run:  

```
docker container run -it -v C:\data:C:\data microsoft/nanoserver powershell
```

Make some files, and run
```
PS C:\> dir data


    Directory: C:\data


Mode                LastWriteTime         Length Name
----                -------------         ------ ----
-a----       11/29/2017  12:54 PM              0 Docker.txt
-a----       11/29/2017  12:54 PM              0 On.txt
-a----       11/29/2017  12:54 PM              0 Windows.txt
```

Similarly, let's play with the Dockerfile. 
```
FROM microsoft/windowsservercore

ENV chocolateyUseWindowsCompression false

RUN powershell -Command \
    iex ((new-object net.webclient).DownloadString('https://chocolatey.org/install.ps1'));
```

And run the container - you now have chocolatey (a package manager for Windows powershell): 
```
chocolatey -?
``` 

Or what about an IIS? 
```
docker run -d -p 80:80 --name iis microsoft/iis
```

Which can be accessed in internet explorer (sorry, only browser there) on this ip: 
```
docker inspect --format '{{ .NetworkSettings.Networks.nat.IPAddress }}' iis
```

This fresh installation does not have golang installed. We can just use a container to fix that. 
Create the following file: 

and run: 

```
docker run -it -v C:\<yourpath>:C:\code golang:nanoserver powershell
cd \code
go build webserver.go
```

Voila. Webserver.exe has been put into the current directory. 

Use the following dockerfile: 
```
FROM microsoft/nanoserver

COPY webserver.go /code/webserver.exe /webserver.exe

EXPOSE 8080

CMD ["\\webserver.exe"]
```

IIS needs to be able to find it later, and it does not run on localhost. So we need to name our container: 
```
docker run -d --name=mysite -p 8080:8080 <yourtag>
```

If you mess it up somehow, and need to kill the container dont forget to run rm mysite. 

You can access it by running: 
```
start http://$(docker inspect -f '{{ .NetworkSettings.Networks.nat.IPAddress }}' mysite):8080
```

This concludes the Windows bit of the workshop for now, but everything you worked with in regards to Docker works with Windows. 
However multi container builds require a newer version of Docker than the Virtual machines have, so this is something you'll have to try at home ;) 