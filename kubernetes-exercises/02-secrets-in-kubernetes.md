# Secrets (and configmaps)
Secrets are a way to store things that you do not want floating around in your code. 

It's things like passwords for databases, API keys and certificates.

Similarly configmaps are for configuration, that doesn't really belong in code but needs to change. Examples include loadbalancer configurations, jenkins configuration and so forth. 

We will look at both these in this coming exercise. 

Our [maginificent app](./secrets/secretapp.js) has it's API key and language hardcoded and will therefore be committed to git for all the world to see. 
If you feel adventurous, you can do this exercise to an app of your choice. 

Keeping credentials in plain sight is slightly problematic, as any security expert will probably preach. 

The first step to fixing it, would be to make our variables as environmental variables.

Change: 
```
  const language = 'English';
  const API_KEY = '123-456-789';
```
To: 
```
  const language = process.env.LANGUAGE;
  const API_KEY = process.env.API_KEY;
```
Now that we are reading from the env variables instead, let's go ahead and dockerize that. 

```
FROM node:9.1.0-alpine
EXPOSE 3000
ENV LANGUAGE English
ENV API_KEY 123-456-789
COPY secretapp.js .
ENTRYPOINT node secretapp.js
```

We can run that in our Kubernetes cluster after pushing it to Docker hub. 

Fill out [the deployment](./secrets/deployment.yml). Notice the env values added in the bottom. 

Run the deployment by writing: 
```
kubectl apply -f yourfile.yml
```

Expose the deployment on a nodeport, so you can see the running container. 

Despite the default value in the Dockerfile, it should be overwritten by the deployment env values! 

However we just moved it from being hardcoded in our app to being hardcoded in our Dockerfile. 

Let's move the API key to a (generic) secret: 
```
kubectl create secret generic apikey --from-literal=API_KEY=oneringtorulethemall
```

Kubernetes supports different kinds of preconfigured secrets, but for now we'll deal with a generic one. 

Similarly for the language into a configmap: 
```
kubectl create configmap language --from-literal=LANGUAGE=Orcish
```

Similarly to all other objects, you can run "get" on them. 

```
kubectl get secrets
kubectl get configmaps
```

Try to describe the secret. 

Last step is to change the Kubernetes deployment file to use the secrets. 

Change:
```
    env:
        - name: LANGUAGE
          value: Polish
        - name: API_KEY
          value: 333-444-555
```

To: 
```
    env:
        - name: LANGUAGE
          valueFrom:
            configMapKeyRef:
              name: language
              key: LANGUAGE
        - name: API_KEY
          valueFrom:
            secretKeyRef:
              name: apikey
              key: API_KEY
```

You should now see the variables being loaded from configmap and secret respectively.

To hot swap the values, you need to keep in mind that pods have a cache, so it becomes a two step process: 

```
kubectl create configmap language --from-literal=LANGUAGE=Elvish -o yaml --dry-run | kubectl replace -f -

kubectl create secret generic apikey --from-literal=API_KEY=andinthedarknessbindthem -o yaml --dry-run | kubectl replace -f -
```

Then delete the pod (so it recreates) : 
```
kubectl delete pod envtest-3380598928-kgj9d
```

This concludes the exercise on secrets and configuration maps. 
