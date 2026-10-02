package fu.hr.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"fu.hr.controller"})
@RequiredArgsConstructor
public class WebMvcConfig {
    private final ApplicationContext applicationContext;

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setOrder(2);

        return  viewResolver;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        // NOTE 'order' and 'viewNames' are optional
        viewResolver.setOrder(1);
//        viewResolver.setViewNames(new String[] {".html", ".xhtml"});
        return viewResolver;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver(){

        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext); // IoC
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");

        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
}
