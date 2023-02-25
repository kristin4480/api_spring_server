FROM openjdk:17.0.2

WORKDIR /home/kris/

COPY target/api_server-0.0.42-SNAPSHOT.jar app.jar

COPY entrypoint.sh .

RUN ["chmod","+x","/home/kris/entrypoint.sh"]

ENTRYPOINT ["bash", "-c", "/home/kris/entrypoint.sh \"${JAVA_OPTS}\" ${@}"]

EXPOSE 8080
