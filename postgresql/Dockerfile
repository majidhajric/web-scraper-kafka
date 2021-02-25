FROM postgres:latest
ARG USER_ID=1000
ARG PORT=5432

EXPOSE $PORT
ENV PORT=$PORT

RUN usermod -u $USER_ID postgres

COPY ./init-scripts/auth-db-init.sh /docker-entrypoint-initdb.d/
COPY ./init-scripts/resource-db-init.sh /docker-entrypoint-initdb.d/

RUN chown -hR postgres /var/lib/postgresql/data
