FROM debian:stretch
ENV DEBIAN_FRONTEND noninteractive
RUN apt-get update; \
    apt-get upgrade -y; \
    apt-get install htop mc -y

COPY  nginx_1.13.10-1_amd64.deb /usr/src

RUN dpkg -i /usr/src/nginx_1.13.10-1_amd64.deb; \
    mkdir -p /opt/nginx/logs; \
    mkdir -p /opt/nginx/html

ADD https://raw.githubusercontent.com/noisypatient/task/master/nginx.conf /opt/nginx/conf/
ADD https://raw.githubusercontent.com/noisypatient/task/master/index.html /opt/nginx/html/

RUN chmod 644 -R /opt/nginx/html/*

CMD /opt/nginx/sbin/nginx -c /opt/nginx/conf/nginx.conf