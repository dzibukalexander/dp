package by.brstu.rec.configurations;

import by.brstu.rec.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .userDetailsService(customUserDetailsService)
//                .authorizeHttpRequests(auth -> auth
//                        //"/api/ml/**"
//                        .requestMatchers("/api/ml/**", "/register", "/login", "templates/css/**", "/js/**").permitAll() // Разрешить доступ к регистрации и статике
//                        .anyRequest().authenticated() // Все остальные запросы требуют авторизации
//                )
//                .formLogin(form -> form
//                        .loginPage("/login") // Страница входа
//                        .defaultSuccessUrl("/") // Перенаправление после успешного входа
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/login") // Перенаправление после выхода
//                        .permitAll()
//                ).build();
//
////        http
////                .authorizeRequests()
////                .antMatchers("/admin/**").hasRole("ADMIN")
////                .antMatchers(HttpMethod.POST, "/admin/positions").permitAll()
////        // ...

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/ml/**", "/register", "/login", "templates/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/positions").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            // Получаем роли пользователя
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

                            // Перенаправляем админа на /admin/users, остальных — на /
                            if (isAdmin) {
                                response.sendRedirect("/admin/users");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}