# ASP framework in containers

Before starting, it is important to be aware of something when working with containers and ASP framework. 

Microsoft is pushing for a specific workflow with ASP framework and Docker, and making the best practice workflow (ie everything as code) is out of the scope of this workshop. 

The workflow proposed by Microsoft, is to run publish (ie build) from Visual Studio, and then package that output into a container. 

To make this correctly, would require us to get our hands real dirty with MSBuild and MSDeploy in powershell - but instead we will run various ready made containers to show off Windows containers as these workshop's focus is on docker. 

To start off, remote desktop to the windows machine given and open powershell. 

Run the familiar hello-world example: 

```
docker run hello-world
```

The interesting thing here, is that command is being executed in powershell. On windows kernel. It is NOT the same hello-world we saw previously. 

Let's ramp things up a bit: 

```
docker run -it microsoft/nanoserver powershell
```

If you want leave out the "powershell" in the end, it will automatically execute cmd which messes a bit with the powershell of your VM.

Exit the container by typing `exit` to exit the container.

If you run `docker image ls`, you'll note that hello-world is built on nanoserver by looking at the size:

```
REPOSITORY                                 TAG                 IMAGE ID            CREATED             SIZE
microsoft/dotnet-framework-samples         latest              e5cc04acc880        13 hours ago        12.4GB
microsoft/mssql-server-windows-developer   latest              9e08a14c562e        3 days ago          11.6GB
hello-world                                latest              b14262c9a790        2 weeks ago         1.1GB
microsoft/windowsservercore                latest              1fbef5019583        3 weeks ago         10.4GB
microsoft/nanoserver                       latest              edc711fca788        3 weeks ago         1.1GB
```

It is a bit bigger than the linux equivalent.. but it does the same thing, and loads an entire windows OS while we are at it. 

The base image normally run in windows is microsoft/windowsservercore - and is what you should base your windows applications on. 

```
docker run -it microsoft/windowsservercore powershell
```

The biggest challenge working with Windows containers in my experience, has been adapting things that are natively provided to run in a container. 

Examples include how to build and deploy a project that normally Visual Studio and IIS took care of. This means the real tradeoff to using Windows containers is learning how Microsoft works under the hood. The gain is that a lot of the common problems with Windows go away. 

> remember to `exit` your container before moving to the next command-

Containers will allow you to spin up things seamlessly, just like on Linux. For example: 

```
docker run -d -p 1433:1433 -e sa_password=YOUR_PWD_HERE -e ACCEPT_EULA=Y microsoft/mssql-server-windows-developer
```

Which spins up a development server for Microsoft SQL. Since the remote desktop machines are not set up with tools, you cannot access it - but a real development machine could just use SQL management tools.

Or how about the entire azure powershell commandline interface?


```
docker run -it azuresdk/azure-powershell powershell
Get-Help Add-AzureRmAccount
```

Microsoft did a pretty good job, making it feel and seem like native docker - because it is. They have an upstream docker fork, that they pull in for releasing docker on windows. 


Let's look at some examples to finish: 
```
docker run microsoft/dotnet-framework-samples
```

or specify a specific ASP framework version:

```
docker run microsoft/dotnet-framework-samples:4.7.1
```
The docker file for the above example looks like this: 

```

FROM microsoft/dotnet-framework-build:4.7.1 as builder
WORKDIR /app
COPY . ./
RUN ["msbuild.exe", "dotnetapp-4.7.1.csproj", "/p:Configuration=Release"]

FROM microsoft/dotnet-framework:4.7.1
WORKDIR /app
COPY --from=builder /app/bin/Release .

ENTRYPOINT ["dotnetapp-4.7.1.exe"]
```

The above dockerfile is also a way to get started with shipping apps natively on Windows, as you can publish your app and copy it in in a similar fashion. Just make use of the servercore image instead.

These are not supposed to be base images, but serve as an exellent demo that Windows is capable of running native containers.

This concludes the ASP framework exercises.

