package de.szut.lf8_project.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@ConditionalOnProperty(value = "keycloak.enabled", matchIfMissing = true)
class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/hello").hasRole("user")
                .antMatchers("/v1/api/pms/project").hasRole("product_owner")
                .antMatchers("/v1/api/pms/project/delete/{id}").hasRole("product_owner")
                .antMatchers("/v1/api/pms/project/read/{id}").hasAnyRole("user", "product_owner")
                .antMatchers("/v1/api/pms/project/read").hasRole("product_owner")
                .antMatchers("/v1/api/pms/project/readEmployees/{id}").hasRole("product_owner")
                .antMatchers("/v1/api/pms/project/update/{id}").hasRole("product_owner")
                .antMatchers("/v1/api/pms/project/{projectId}/{employeeId}").hasRole("product_owner")
                .antMatchers("/v1/api/pms/employee/{id}").hasRole("product_owner")
                .anyRequest()
                .permitAll();
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) {
        final var keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}