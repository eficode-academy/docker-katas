# Remote clusters

Having played with minikube, it is pretty simple to spin up things local. 

But eventually you will need a production grade cluster, and you will need to understand how the structure of one looks. 

kubectl is set up to use a configuration, which can be viewed by running: 

```
kubectl config view
```

This is a file, usually located in homefolder/.kube/config.

Connecting to a different cluster can be done in a multitude of different ways, also by creating your own context (config) and so on. 

For the remainder of the workshop, you will be given the config file for a google cloud Kubernetes cluster, which you can then use. 

Overwrite the /home/.kube/config file with the given one, and run 

```
kubectl config use-context gke_docker-training-185510_europe-west1-b_cluster-1
kubectl get pods
```

This should be a different cluster than previously, which you can verify by looking at the age of the pods. 

Alternatively should you wish to have multiple config files, it is possible to specify a cluster each command like so: 

```
kubectl get pods --kubeconfig myconfig.yml
```

This quickly becomes tiresome. 

Since we are multiple people sharing a cluster, you will be required to create a namespace, named after you: 

```
kubectl create namespace david
kubectl get pods -n david
```

Namespaces are the default way for kubernetes to seperate resources. Namespaces do not share anything between them, which is important to know. 

For example, if you have a secret password stored in namespace "test", and a different one in namespace "production", a deployment sent to the test namespace will use the test secret, and one sent to production will use another one. 

This is quite powerful. On a user level, it is also possible to limit namespaces and resources by users but it is a bit too involved for your first experience with Kubernetes. Therefore, please be aware that other people's namespaces are off limits for this workshop even if you do have access. ;) 

Every time you run a kubectl command, you can opt to use the namespace flag (-n mynamespace) to execute the command on that namespace. 

Similarly in the yaml files, you can specify namespace. Most errors you will get throughout the rest of the workshop will 99% be deploying into a namespace where someone already did that before you so ensure you are using your newly created namespace!

Try deploying nginx in your new namespace: 

```
kubectl run nginx --image=nginx --replicas=3 -n yournamespace
```

In the beginning you will feel more comfortable with the kubectl commands, than with the equivalent in yaml. However keeping things as code is important! 

If you delete a namespace for example, everything running in the namespace is similarly deleted. Having everything as code makes spinning it up again quite easy. 

Extract the yaml from Kubernetes for your new nginx deployment. 

```
kubectl get deployment nginx -o yaml -n somenamespace > nginx.yml
```

Have a look at the file. This is how Kubernetes sees the deployment. A lot of these things can be cleaned up, and default values will be given when you deploy. 

For example creationTimestamp is not good practice to keep in a yaml file. 

The yaml files for the coming exercises will give you a better impression of what is sensible.

This concludes this workshop :) 