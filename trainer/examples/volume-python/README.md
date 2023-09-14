The idea for this one is to make a small python application that prints "Hello world!" to your terminal, and volume mount in the code from the host.

It is a pre-example to building your own image.

## Exercise
You need a terminal in this folder.

Run:

`docker run -v "$PWD":/usr/src/myapp -w /usr/src/myapp python:3 python main.py`