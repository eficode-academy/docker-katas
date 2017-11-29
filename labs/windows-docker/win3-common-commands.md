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