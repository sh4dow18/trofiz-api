package sh4dow18.trofiz
// Security Configuration Dependencies
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
// Tag that establishes that this configuration would be only for "init" spring profile
@Profile("init")
// Tag that establishes that this would be a Spring Configuration
@Configuration
// Tag that establishes that this would activate the Spring Web Security and this one
// personalize the original spring security configuration
@EnableWebSecurity
// Close Security Configuration class definition
class CloseSecurityConfiguration{
    // Tag that establishes that this is a Spring Bean
    @Bean
    // Function that creates and configure the Spring Security Filter Chain
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // It disables the protection for Cross-Site Request Forgery (CSRF) Attacks
            .csrf { csrf ->
                csrf.disable()
            }
            // It establishes that any request would be authenticated to access to the information
            .authorizeHttpRequests {
                it
                    .anyRequest().authenticated()
            }
        // Create the Spring Security Filter Chain
        return http.build()
    }

}
// Tag that establishes that this configuration would be only for "dev" spring profile
@Profile("!init")
// Tag that establishes that this would be a Spring Configuration
@Configuration
// Tag that establishes that this would activate the Spring Web Security and this one
// personalize the original spring security configuration
@EnableWebSecurity
// Tag that establishes that the security level would be in method level
@EnableMethodSecurity
// Jwt Security Configuration class definition
class JwtSecurityConfiguration {
    // URL Variables Declarations
    // The value would be taken of "application.properties"
    @Value("\${public.endpoint}")
    val publicEndpoint: String? = null
    // Resource that is used to load the "User" authenticated information in this Process
    // This would be with "AppUserDetailsService" service
    @Resource
    private val userDetailsService: AppUserDetailsService? = null
    // Tag that establishes that this is a Spring Bean
    @Bean
    // Tag that establishes that this would throw a General Exception
    @Throws(java.lang.Exception::class)
    // Creates and configure an Authentication Manager for Spring Security
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager? {
        return authConfig.authenticationManager
    }
    // Tag that establishes that this is a Spring Bean
    @Bean
    // Creates and configure an BCrypt Password Encoder for Spring Security
    fun passwordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }
    // Tag that establishes that this is a Spring Bean
    @Bean
    // Creates and configure a Dao Authentication Provider for Spring Security
    // This is used to authenticate "User"
    fun authenticationProvider(): DaoAuthenticationProvider? {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }
    // Tag that establishes that this is a Spring Bean
    @Bean
    // Function that creates and configure the Spring Security Filter Chain
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // It disables the protection for Cross-Site Request Forgery (CSRF) Attacks
            .csrf { csrf ->
                csrf.disable()
            }
            // It establishes the CORS Configuration Source
            .cors { cors ->
                cors.configurationSource(corsConfigurationSource())
            }
            // It establishes the request that are allowed and how these are allowed
            .authorizeHttpRequests {
                it
                    // If the URL starts with the value of "URL_PUBLIC", permit all the requests
                    .requestMatchers(publicEndpoint.plus("/**")).permitAll()
                    // Any other, would need authentication
                    .anyRequest().authenticated()
            }
            // It establishes the Session Management Policy as Stateless, that means that would not
            // have sessions in the server, this is because the application would use JWT
            .sessionManagement{
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // Configure the Authentication Provider
            .authenticationProvider(authenticationProvider())
            // Applies an Additional Custom Security Configurations
            .with(AppCustomDsl.customDsl()) { AppCustomDsl.customDsl() }
        // Create the Spring Security Filter Chain
        return http.build()
    }
    // Tag that establishes that this is a Spring Bean
    @Bean
    // Creates and Configure a CORS Configuration Source that defines the origins, headers
    // and methods that are allowed in the requests
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("http://localhost:8080")
        config.allowedHeaders = listOf("*")
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.exposedHeaders = listOf("Authorization")
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
// App Custom DSL Class Definition that extends of "AbstractHttpConfigurer" that is used to
// custom the Spring security configuration
class AppCustomDsl : AbstractHttpConfigurer<AppCustomDsl?, HttpSecurity?>() {
    // Function that is used to configure specific aspects in the security
    override fun configure(http: HttpSecurity?) {
        // Add all the build-in configuration
        super.configure(builder)
        // Obtains a Shared Instance of "AuthenticationManager" of "HTTPSecurity"
        val authenticationManager = http?.getSharedObject(
            AuthenticationManager::class.java
        )
        // Add the Additional Security Filters created in Security File
        http?.addFilter(JwtAuthenticationFilter(authenticationManager!!))
        http?.addFilter(JwtAuthorizationFilter(authenticationManager!!))
    }
    // Defines a Companion Object to "AppCustomDsl" class
    companion object {
        // Creates and Returns an instance of "AppCustomDsl" to apply in Spring Security
        fun customDsl(): AppCustomDsl {
            return AppCustomDsl()
        }
    }

}