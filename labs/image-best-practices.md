# Best practises

## dockerignore

Before the docker CLI sends the context to the docker daemon, it looks for a file named `.dockerignore` in the root directory of the context. If this file exists, the CLI modifies the context to exclude files and directories that match patterns in it. This helps to avoid unnecessarily sending large or sensitive files and directories to the daemon and potentially adding them to images using ADD or COPY.

> For more info on dockerignore, head over to the [documentation](https://docs.docker.com/engine/reference/builder/#dockerignore-file).

## Lint your Dockerfile

[Hadolint](https://hadolint.github.io/hadolint/) highlights dubious constraints in your `Dockerfile`.

The linter uses the principles described in [Docker's documentation on best practices](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/) as the basis for the suggestions.

## Consider security when building images

[Snyk](https://snyk.io/blog/10-docker-image-security-best-practices/) wrote a blog with 10 things that you should consider when building images. They consider adding a label for the security policy of
the image, using a linter (as described above), and [signing docker images](https://docs.docker.com/notary/getting_started/).
