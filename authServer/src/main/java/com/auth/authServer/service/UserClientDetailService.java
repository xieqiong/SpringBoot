package com.auth.authServer.service;

import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserClientDetailService implements ClientDetailsService, ClientRegistrationService {
    @Override
    public ClientDetails loadClientByClientId(String ClientId) throws ClientRegistrationException {
        BaseClientDetails baseClientDetails = new BaseClientDetails();
        // 数据库操作
        return baseClientDetails;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        // 数据库操作
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
      // 数据库操作
    }

    @Override
    public void updateClientSecret(String s, String s1) throws NoSuchClientException {
      //数据库操作
    }

    @Override
    public void removeClientDetails(String s) throws NoSuchClientException {

    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return null;
    }
}
