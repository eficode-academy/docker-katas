# Setting up a single node cluster
For development it is necessary to be able to deploy services locally. 

Normally the way you would do this is to deploy in a development namespace on your production cluster, but here we are going to get a simple little cluster up and running in no time. 

Before we can do that however, we need to get [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) which is short for kubernetes controller.

```
curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.7.5/bin/linux/amd64/kubectl && chmod +x ./kubectl && sudo mv ./kubectl /usr/local/bin/kubectl
```

Kubectl will be covered a little later, for now let's look at [minikube](https://github.com/kubernetes/minikube) - Minikube is a part of the Kubernetes open source project, with the single goal of getting a simple cluster up and running with just one virtual machine acting as node. 

Go back to your linux machine, and run: 
```
curl -Lo minikube https://storage.googleapis.com/minikube/releases/v0.22.0/minikube-linux-amd64 && chmod +x minikube && sudo mv minikube /usr/local/bin/
```

Which will download minikube and put it in path allowing you to run: 

```
minikube start
```

This should tell you that kubectl is configured to use your cluster.
You can verify everything is set up correctly by running 
```
kubectl get nodes
``` 

Which gives an overview of all nodes in the cluster, in this case just minikube's node.

So let's try to use some of the Kubernetes objects, starting with a deployment. 

```
kubectl run dotnet --image=sharor/hello-world --replicas=3
```

We can check the 3 containers are running: 

```
kubectl get pods

```

To look closer at a pod, you can describe it: 

```
kubectl describe pod dotnet-<unique-pod-name>
```
However the pods are currently not accessible, since no port forwarding is happening to the container. 

We need to set up another Kubernetes object - a service. Think of a service as a port and ip endpoint, allowing you to reach a container. We tell it which port to target (for dotnet core it is 5000) and what type of service, here it is NodePort which also opens an external port on the Kubernetes node.

```
kubectl expose deployment dotnet --type=NodePort --port=5000
```

Similarly to how it was done for a pod, you can describe a service. Here we need the NodePort: 
```
kubectl describe svc dotnet | grep NodePort
```

Which should return a port above 30000, which is serving our container. Since the port is serving as a NodePort, we need to hit a node in the cluster. 

Now, we only have one node but if we had multiple then the IP of ANY of them would connect us to our container. 

Minikube has a nice little variable for finding the node ip: 

```
echo $(minikube ip)
```

Simply accessing the ip on the correct port will then direct the traffic to one of the healthy containers. Like so: 

```
http://192.168.99.100:32112
```

Finally Minikube comes with a nice gui tool to create an overview of things served in the cluster. 

To see it, just run: 
```
minikube dashboard
```

This concludes the exercise, happy coding!