FROM jboss/keycloak:latest

COPY docker-entrypoint.sh /opt/jboss/tools
COPY --chown=jboss:root realms/realm-export.json /opt/jboss/tools/import/demo-realm.json

ENV KEYCLOAK_IMPORT /opt/jboss/tools/import/demo-realm.json

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]
CMD ["-b", "0.0.0.0"]
