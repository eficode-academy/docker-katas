FROM centos:7

CMD ["/usr/sbin/init"]

# Delete everything inside `/lib/systemd/system/sysinit.target.wants/`, except `systemd-tmpfiles-setup.service`
RUN ( cd /lib/systemd/system/sysinit.target.wants/; \
      for i in *; do [ $i == systemd-tmpfiles-setup.service ] || rm -f $i; done ) \
 && rm -f /lib/systemd/system/multi-user.target.wants/* \
          /etc/systemd/system/*.wants/* \
          /lib/systemd/system/local-fs.target.wants/* \
          /lib/systemd/system/sockets.target.wants/*udev* \
          /lib/systemd/system/sockets.target.wants/*initctl* \
          /lib/systemd/system/basic.target.wants/* \
          /lib/systemd/system/anaconda.target.wants/* \
 && yum -y install postfix dovecot httpd \
 && yum clean all \
 && systemctl enable postfix dovecot httpd 

# Mount the following at container runtime:
# -v /sys/fs/cgroup:/sys/fs/cgroup:ro
# --tmpfs /run

VOLUME [ "/sys/fs/cgroup" ]

# Expose necessary ports
EXPOSE 80 443 25 110 143 443 993 995


# Build and run instructions:
#############################

# docker build -t local/centos7:mailserver
# docker run -p 80:80 -p 25:25 \
#  -v /sys/fs/cgroup:/sys/fs/cgroup:ro \
#  --tmpfs /run \
#  -d local/centos7:mailserver

