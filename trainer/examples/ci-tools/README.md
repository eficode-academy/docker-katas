
# Docker CI tools
Note, you need to have run the "building flask on different OS" example first.

## Hadolint
https://github.com/hadolint/hadolint

```
docker run --rm -i hadolint/hadolint < Dockerfile
docker run --rm -i hadolint/hadolint < Dockerfile-python
docker run --rm -i hadolint/hadolint < Dockerfile-python-alpine
```

## Trivy
See the different severities you get by the different OS.

```bash
docker run -v /var/run/docker.sock:/var/run/docker.sock -v aquacache:/root/.cache aquasec/trivy image mypy:latest
docker run -v aquacache:/root/.cache aquasec/trivy image mypy:python
docker run -v aquacache:/root/.cache aquasec/trivy image mypy:python-alpine

```