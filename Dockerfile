# amazon image with only Java 11 JRE installed. Lightweight image without bloat.
FROM amazoncorretto:11-al2022-jdk

RUN yum install -y shadow-utils

# Creating group and user for application
RUN groupadd --system appgroup --gid 1000 && adduser --system appuser -g appgroup --uid 1000

# Adding timezone capability and setting timezone in container to Europe/Stockholm
RUN yum install -y tzdata
ENV TZ Europe/Stockholm

# Setting workdir to users home directory
WORKDIR /home/appuser/app

# Creating logs directory
RUN mkdir logs && chown appuser logs && chmod -R 764 logs

# Moving application jar into current workdir
COPY target/product-catalogue-1.0.jar application.jar

# Exposing port 5000 from the docker container, since application runs on port 5000
EXPOSE 5000

# lower privileges from root to the user we created above
USER appuser

# command run on container start. Process will run as PID 1 inside the container.
ENTRYPOINT ["java", "-jar", "application.jar"]