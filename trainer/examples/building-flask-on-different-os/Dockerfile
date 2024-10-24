FROM ubuntu:22.04
RUN apt-get update && apt-get install -y \
 python3-pip \
 python3-dev \
 build-essential
COPY requirements.txt /usr/src/app/
RUN pip3 install --no-cache-dir -r /usr/src/app/requirements.txt
COPY app.py /usr/src/app/
EXPOSE 5000
CMD ["python3", "/usr/src/app/app.py"]