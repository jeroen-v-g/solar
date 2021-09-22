package nl.jemo85.oauth.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final ClientRegistrationRepository clientRegistrationRepository;

  public WebSecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
    this.clientRegistrationRepository = clientRegistrationRepository;
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.cors().configurationSource(corsConfigurationSource()).and()
        .authorizeRequests(exchanges -> exchanges
            .antMatchers("/", "/error", "/*.js", "/*.css", "/*.ico", "/*.map", "/security/isAllowed", "/menu/get")
            .permitAll().anyRequest().authenticated())
        .oauth2Login(oauth2Login -> oauth2Login.clientRegistrationRepository(clientRegistrationRepository)
            .userInfoEndpoint().userAuthoritiesMapper(userAuthoritiesMapper()))
        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(oidcLogoutSuccessHandler()));
  }

  private LogoutSuccessHandler oidcLogoutSuccessHandler() {
    OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(
        this.clientRegistrationRepository);

    // Sets the `URI` that the End-User's User Agent will be redirected to
    // after the logout has been performed at the Provider
    oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8090");

    return oidcLogoutSuccessHandler;
  }

  private GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

      authorities.forEach(authority -> {
        if (OidcUserAuthority.class.isInstance(authority)) {
          OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

          OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
          if (userInfo.getClaim("realm_access") != null) {
            Map<String, List<String>> roles = userInfo.getClaim("realm_access");
            for (String roleName : roles.get("roles"))
              mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
          }
        } else if (OAuth2UserAuthority.class.isInstance(authority)) {
          OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
          Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

          if (userAttributes.containsKey("role")) {
            String roleName = "ROLE_" + (String) userAttributes.get("role");
            mappedAuthorities.add(new SimpleGrantedAuthority(roleName));
          }
        }
      });
      return mappedAuthorities;
    };
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    var configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
