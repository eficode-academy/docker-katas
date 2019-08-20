# Testing unicode support

```
docker build -f Файлдокера -t unicode:latest .
docker run -it unicode:latest
```

Sadly, Docker doesn't support unicode in image names and tags. 😢