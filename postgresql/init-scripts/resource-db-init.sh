#!/usr/bin/env bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER ${RESOURCE_DATABASE_USER} WITH PASSWORD '${RESOURCE_DATABASE_PASSWORD}';
    CREATE DATABASE ${RESOURCE_DATABASE_NAME};
    GRANT ALL PRIVILEGES ON DATABASE ${RESOURCE_DATABASE_NAME} TO ${RESOURCE_DATABASE_USER};
EOSQL
