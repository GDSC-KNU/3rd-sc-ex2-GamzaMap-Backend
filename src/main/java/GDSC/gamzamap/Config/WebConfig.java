package GDSC.gamzamap.Config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

   @Bean
   public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean(){
       CorsConfiguration config=new CorsConfiguration();
       config.setAllowCredentials(false);
       config.addAllowedOrigin("*");
       config.addAllowedHeader("*");
       config.addAllowedMethod("*");
       config.setMaxAge(6000L);

       UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**",config);

       FilterRegistrationBean<CorsFilter> filterBean = new FilterRegistrationBean<>(new
               CorsFilter());
       filterBean.setOrder(0);

       return filterBean;
   }

}
