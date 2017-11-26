# Dotnet core in Docker

Before starting with .NET in docker, it is important to know the following: 
- The ASP framework is supported, but is done differently. 
- .NET core runs natively in linux docker (we start with this) 
- Microsoft is doing a lot of development on Docker for Windows, and are constantly improving the entire ecosystem. Things that did not work last week might work now. 

And since there is always people wondering if it is production ready - .NET core has been production capable since 2.0 according to microsoft, and Kubernetes has support for Windows nodes. 

Anyway let's get started ! 

```
docker container run -it -p 5000:5000 microsoft/dotnet
```

We expose port 5000 preemptively, since that is what our app will run on. 

Inside the container, make a new directory (dotnet has issues running in root directory) and then make a new project:  

```
mkdir myapp
cd myapp && dotnet new razor
```

The project should automatically restore nuget packages, but in the unlikely case it did not you can run : 

```
dotnet restore
```

.NET has a thing with containers, where it needs to expose an environmental variable telling the environment where kestrel (.NET webserver) is allowed to host solutions: 

```
export ASPNETCORE_URLS=http://*:5000
```

And then just run the app: 

```
dotnet run
```

Go to localhost:5000 on your machine, you should have a fresh web app running (razor pages). 