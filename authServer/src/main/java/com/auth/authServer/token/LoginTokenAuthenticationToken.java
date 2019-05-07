package com.auth.authServer.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;


public class LoginTokenAuthenticationToken extends AbstractAuthenticationToken {

    private Object principal;

    public LoginTokenAuthenticationToken(Object principal){
        super(null);
        this.principal = principal;
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if(authenticated){
            try {
                throw new IllegalAccessException("CANNOT SET");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        super.setAuthenticated(authenticated);
    }
}
