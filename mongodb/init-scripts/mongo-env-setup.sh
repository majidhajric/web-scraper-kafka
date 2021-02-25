#!/bin/sh

envsubst < /docker-entrypoint.d/create-user.js.template > /docker-entrypoint.d/create-user.js

