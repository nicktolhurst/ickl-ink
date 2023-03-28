#-- BUILD CONTAINER --#
FROM clojure:tools-deps AS BUILD

WORKDIR /home

COPY . .

RUN clj -A:fig:min

# #-- RUNTIME CONTAINER --#
FROM nginx AS WEB

COPY --from=BUILD /home/resources/public/ /usr/share/nginx/html/
COPY --from=BUILD /home/target/public/cljs-out/dev-main.js /usr/share/nginx/html/cljs-out/dev-main.js