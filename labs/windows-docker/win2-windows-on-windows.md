# ASP framework in containers

Before starting, it is important to be aware of something when working with containers and ASP framework. 

Microsoft is pushing for a specific workflow with ASP framework and Docker, and making the best practice workflow (ie everything as code) is out of the scope of this workshop. 

The workflow proposed by Microsoft, is to run publish (ie build) from Visual Studio, and then package that output into a container. 

To make this correctly, would require us to get our hands real dirty with MSBuild and MSDeploy in powershell - but instead we will run various ready made containers to show off Windows containers as these workshop's focus is on docker. 

To start off, remote desktop to the windows machine given and open powershell. 