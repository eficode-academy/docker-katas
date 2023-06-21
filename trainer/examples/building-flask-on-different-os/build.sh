# /bin/bash
docker build -t flask-app .
docker build -f Dockerfile-python -t flask-app:python .
docker build -f Dockerfile-python-alpine -t flask-app:alpine .
