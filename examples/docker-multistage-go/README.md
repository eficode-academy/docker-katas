# Multi-stage Docker Build for Go Applications

This repository shows how to create a multi-stage Docker build for a minimal Go application.

## Usage

- Build: `docker image build -t people/multistage-go .`
- Run: `docker container run people/multistage-go`

## Output

```shell
hello world
```

## Notice

The size of the image you created is less than `2MB`,
    you can verify this by running the command `docker image ls`.
    This is because the final image doesn't contain the tools
    needed to compile a Go application;
    just the standalone Go application itself.
