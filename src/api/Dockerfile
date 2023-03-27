#-- BUILDER CONTAINER --#
FROM clojure:openjdk-15-tools-deps AS BUILD

ADD deps.edn /deps.edn
RUN clj -Sdeps '{:mvn/local-repo "./src/linkshortener"}'
COPY ./uberdeps /uberdeps
COPY ./src /src

RUN chmod +x /uberdeps/package.sh
RUN /uberdeps/package.sh


#-- RUNTIME CONTAINER --#
FROM openjdk:15-slim-buster AS API

COPY --from=BUILD /target/project.jar /project.jar

EXPOSE 8888

ENTRYPOINT ["java", "-cp", "project.jar", "clojure.main", "-m", "linkshortener.core"]