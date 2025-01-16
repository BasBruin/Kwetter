//package nl.kwetter2.apigateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
//import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
//import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
//import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
//
//import java.util.List;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//
//    private static final String ADMIN = "ADMIN";
//
//    private static final String[] BUSINESS_EDITORS = new String[] {
//            ADMIN,
//    };
//
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
//                                                            ReactiveAuthenticationManager authenticationManager,
//                                                            ServerAuthenticationConverter authenticationConverter) {
//        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(
//                authenticationManager);
//        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);
//
//        http.authorizeExchange(authorizeRequests -> authorizeRequests
//
//                        .pathMatchers("/auth/protected")
//                        .hasAnyRole(ADMIN)
//
//
//                        .anyExchange().authenticated())
//                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//                .httpBasic(HttpBasicSpec::disable)
//                .formLogin(FormLoginSpec::disable)
//                .csrf(CsrfSpec::disable);
//
//        return http.build();
//    }
//}
