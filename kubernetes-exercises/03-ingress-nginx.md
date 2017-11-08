# Ingress
If we look at Service as an endpoint, then the Kubernetes ingress object is a DNS.
It takes care of things like load balancing traffic, terminating SSL and naming among other things. 

To enable an ingress object, we need an ingress controller. In this example we will be using [NGINX](https://www.nginx.com/). If you prefer [Træfik (optimized for Kubernetes) there is another exercise doing it with Træfik](./03-ingress-traefik.md).

To get started with NGINX ingress, we (re)deploy an app of our choice: 
```
kubectl run ingress-test --image=<your-image> --replicas=3
kubectl expose deployment ingress-test --port=<your-port>
```

The NGINX ingress controller requires a default backend which serves as a fallback for nginx in case a request fails. 
It will:
- Serve a 404 page at /
- Serve 200 on /healthz

The [example deployment file for nginx-ingress-controller](ingress-nginx/nginx-backend.yml) is taken from [kubernetes/ingress-nginx](https://github.com/kubernetes/ingress-nginx), as is the [service exposing the backend](ingress-nginx/nginx-service.yml).

They are automatically deployed by enabling ingress, which we will do later.

We will need a certificate for NGINX, which has been prepared in [a script](ingress-nginx/self-signed-cert.sh). We will need to turn that into a secret.

When prompted to put a "common name" in the certificate, write myapp.local (*important!*). This is going to be our host rule for our ingress later.

```
./ingress-nginx/self-signed-cert.sh
kubectl create secret tls tls-certificate --key tls-key.key --cert tls-cert.crt
kubectl create secret generic tls-dhparam --from-file=dhparam.pem
```

We then have to enable ingress, which is very simple:
```
minikube addons enable ingress
```
The reason it is not natively enabled, is because early versions of Kubernetes shipped without it. 

Enabling gave us: 
- A configmap 
    * kubectl get configmap nginx-load-balancer-conf -n kube-system
- The nginx-ingress-controller
    * kubectl get rc nginx-ingress-controller -n kube-system
- A service exposing default NGINX backend pod handling
    * kubectl get svc default-http-backend -n kube-system


Nginx can be accessed this way : 
```
curl $(minikube service nginx-ingress --url)
```

Which will return 404 default backend. 

TODO make myapp work

Access it on http://myapp.local. 

This concludes the exercise for ingress. 

