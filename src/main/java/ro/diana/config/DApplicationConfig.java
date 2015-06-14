package ro.diana.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;



@Configuration
@ComponentScan(basePackages = {"ro.diana"})
@ImportResource( { "classpath*:spring-security.xml" } )
@EnableWebMvc
@Import({DDataSourceConfig.class})
@PropertySource("classpath:app.properties")
public class DApplicationConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private void initProperties(Environment environment) {
        DProperties instance = DProperties.getInstance();
        instance.setClientRootUrl(environment.getRequiredProperty("clientRootUrl"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/protected/**").addResourceLocations("/protected/");
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
        return new DefaultWebSecurityExpressionHandler();
    }

}
