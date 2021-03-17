FROM nginx
ARG PORT=4200
EXPOSE $PORT
ENV PORT=$PORT

RUN rm -rf /etc/nginx/sites-enabled/*
RUN rm -rf /usr/share/nginx/html/*
RUN rm -rf /etc/nginx/conf.d/*

COPY /dist/app/browser /usr/share/nginx/html/
COPY ./nginx/templates/default.conf.template /etc/nginx/templates/

COPY ./nginx/ng-env-setup.sh /docker-entrypoint.d/
