
function getEnv(key: string): any {
  if (!window.hasOwnProperty('__env')) {
    return null;
  }
  const globalEnv = (window as any).__env;
  if (key in globalEnv) {
    return globalEnv[key];
  }
  return null;
}

export const environment = {
  production: getEnv('production') || false,
  debug: getEnv('debug') || !getEnv('production') || true,
  apiServer: getEnv('apiServer') || 'http://localhost:8080/api',
  authServer: getEnv('authServer') || 'http://localhost:9090/auth/realms/scraper',
  authClientId: getEnv('authClientId') || 'scraper'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
