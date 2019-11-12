Make sure you set up some sort of DNS / name resolution from your computer to the IP address where reverse proxy is running.

For example, your VM in the cloud has IP address `1.2.3.4` , and you have your reverse proxy running in that VM as a docker container. In that case, on your local / work computer, setup the following:

```
$ cat /etc/hosts
127.0.0.1 localhost 
1.2.3.4   example.com www.example.com
```

**Note:** For LetsEncrypt to be able to give you certs, your public IP must be reachable through the DNS name that you are using in your labels/traefik rules, .e.g `example.com`.
