import { MsalGuardConfiguration, MsalInterceptorConfiguration } from '@azure/msal-angular';
import { InteractionType, PopupRequest, RedirectRequest } from '@azure/msal-browser';

// Configuración de Azure AD
export const msalConfig = {
  auth: {
    clientId: 'f39f0c7e-8540-428a-978c-8e7960d98c4e', // Tu Application (client) ID
    authority: 'https://login.microsoftonline.com/7d4b93be-78da-4619-a73d-c99a4ec80d71', // Tenant ID correcto
    redirectUri: window.location.origin,
    postLogoutRedirectUri: window.location.origin
  },
  cache: {
    cacheLocation: 'localStorage',
    storeAuthStateInCookie: false
  }
};

// Scopes para el login inicial
export const loginRequest: PopupRequest | RedirectRequest = {
  scopes: ['user.read', 'openid', 'profile']
};

// Endpoints protegidos y sus scopes
export const protectedResources = {
  graphApi: {
    endpoint: 'https://graph.microsoft.com/v1.0/me',
    scopes: ['user.read']
  },
  backendApi: {
    endpoint: 'http://localhost:8080/api',
    scopes: [`api://f39f0c7e-8540-428a-978c-8e7960d98c4e/access_as_user`] // Ajusta este scope según tu configuración en Azure
  }
};

// Guard configuration
export const msalGuardConfig: MsalGuardConfiguration = {
  interactionType: InteractionType.Redirect,
  authRequest: loginRequest
};

// Interceptor configuration
export const msalInterceptorConfig: MsalInterceptorConfiguration = {
  interactionType: InteractionType.Redirect,
  protectedResourceMap: new Map([
    [protectedResources.graphApi.endpoint, protectedResources.graphApi.scopes],
    [`${protectedResources.backendApi.endpoint}/*`, protectedResources.backendApi.scopes]
  ])
};