package pl.gromada.vaadin_project_yerba.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Disables cross-site request forgery (CSRF) protection, as Vaadin already has CSRF protection.
        http.csrf().disable()

                //Uses CustomRequestCache to track unauthorized requests so that users
                // are redirected appropriately after login.
                .requestCache().requestCache(new CustomRequestCache())

                //Turns on authorization.
                .and().authorizeRequests()


                //Allows all internal traffic from the Vaadin framework.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

                //Allows all authenticated traffic.
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated()


                //Enables form-based login and permits unauthenticated access to it.
                .and().formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)

                //Configures the logout URL.
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);

        //H2-Console is not showing in browser
         http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, 'true' FROM user WHERE username=?")
                .authoritiesByUsernameQuery("SELECT username, role FROM user_user_role " +
                        "INNER JOIN user ON user.id_user = user_user_role.id_user " +
                        "INNER JOIN user_role ON user_role.id_user_role = user_user_role.id_user_role " +
                        "WHERE username=?");
    }

    //Exclude Vaadin-framework communication and static assets from Spring Security.
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/h2-console/**");
    }
}
