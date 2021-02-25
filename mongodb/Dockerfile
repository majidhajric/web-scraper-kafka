FROM mongo:latest
ARG USER_ID=1000
ARG PORT=27017

EXPOSE $PORT
ENV PORT=$PORT

RUN usermod -u $USER_ID mongodb

COPY init-scripts/create-user.js.template /docker-entrypoint-initdb.d/
COPY init-scripts/mongo-env-setup.sh /docker-entrypoint.d/

RUN chown -hR mongodb /data/db
