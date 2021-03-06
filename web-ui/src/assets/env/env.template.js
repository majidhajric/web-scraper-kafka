(function(window) {
  window.__env = window.__env || {};

  // Environment variables
  window.__env.production = "${PRODUCTION}";
  window.__env.apiServer = "${API_SERVER}";
  window.__env.authServer = "${AUTH_SERVER}";
  window.__env.authClientId = "${AUTH_CLIENT_ID}";
}(this));
