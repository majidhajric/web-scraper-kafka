FROM node:latest  as NG_BUILDER
RUN npm install -g @angular/cli

RUN mkdir -p /tmp/build
WORKDIR /tmp/build

COPY package.json package-lock.json /tmp/build/
RUN npm install
ENV PATH /tmp/build/node_modules/.bin:$PATH

COPY ./ /tmp/build/
RUN npm run ngcc
RUN npm run build -- --output-path=./dist/out

FROM nginx
ARG PORT=4200
EXPOSE $PORT
ENV PORT=$PORT

RUN rm -rf /etc/nginx/sites-enabled/*
RUN rm -rf /usr/share/nginx/html/*
RUN rm -rf /etc/nginx/conf.d/*

COPY --from=NG_BUILDER /tmp/build/dist/out /usr/share/nginx/html/
COPY ./nginx/templates/default.conf.template /etc/nginx/templates/

COPY ./nginx/ng-env-setup.sh /docker-entrypoint.d/
