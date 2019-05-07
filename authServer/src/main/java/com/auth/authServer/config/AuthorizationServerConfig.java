package com.auth.authServer.config;

import com.auth.authServer.service.UserAuthTokenGranter;
import com.auth.authServer.service.UserAuthTokenService;
import com.auth.authServer.service.UserClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
//  配置对象允许你注入一个你自定义的 OAuth2RequestFactory，因此你可以使用这个特性来设置这个工厂对象
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserClientDetailService userClientDetailService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final String secret="123";

    @Bean
    public TokenStore tokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public UserAuthTokenService userAuthTokenService() {
        UserAuthTokenService userAuthTokenService = new UserAuthTokenService();
        userAuthTokenService.setTokenStore(tokenStore());
        userAuthTokenService.setClientDetailsService(userClientDetailService);
        return userAuthTokenService;
    }


    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(secret);
        return converter;
    }

    // 授权端点开放
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // tokenKeyAccess 开启/oauth/token_key验证端口无权限访问
        // checkTokenAccess 开启/oauth/check_token验证端口认证权限访问
        security.tokenKeyAccess("permitAll")
                .allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()");
    }

    // 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
    // 你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(userClientDetailService);
    }

    //用来配置授权(authorization)以及令牌(token)的访问端点和令牌服务(token services)。
    // 定义授权链接 定义token生产
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 1、配置验证端点，默认的端点和替换的端点。
        // 2、定义token存储方式
        // 3、tokenGranter：这个属性就很牛B了，当你设置了这个东西（即 TokenGranter 接口实现），
        // 那么授权将会交由你来完全掌控，并且会忽略掉上面的这几个属性，这个属性一般是用作拓展用途的，
        // 即标准的四种授权模式已经满足不了你的需求的时候，才会考虑使用这个。
        // 4、token服务 ResourceServerTokenServices 类的实例，用来实现令牌服务。
        endpoints.pathMapping("/oauth/token", "/login")
                .tokenStore(tokenStore())
                .tokenGranter(new UserAuthTokenGranter(authenticationManager,endpoints.getTokenServices(),endpoints.getClientDetailsService(),
                        endpoints.getOAuth2RequestFactory()))
                .tokenServices(new UserAuthTokenService())
                .accessTokenConverter(accessTokenConverter());
    }
}
