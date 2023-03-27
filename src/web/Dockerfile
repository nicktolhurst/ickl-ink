#-- BUILDER CONTAINER --#
FROM clojure:tools-deps AS builder

COPY resources /resources

COPY . .

RUN clj -A:fig:min

COPY target /target
COPY resources /resources


#-- RUNTIME CONTAINER --#
FROM nginx AS WEB

RUN rm /usr/share/nginx/html/*
COPY --from=builder /resources/public/ /usr/share/nginx/html/
COPY --from=builder target/public/cljs-out/dev-main.js /usr/share/nginx/html/cljs-out/dev-main.js

# ENTRYPOINT ["java", "-cp", "project.jar", "clojure.main", "-m", "ickl-ink.core"]