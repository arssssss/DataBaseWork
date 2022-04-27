package zyc.work.databasework.configuration;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zyc.work.databasework.interceptor.token.TokenInterceptor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class MvcConfiguration {

    @Autowired
    public TokenInterceptor tokenInterceptor;

    /**
     * 自定义mvc配置
     * @return
     */
    @Bean
    WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {

            /**
             * 自定义类型转换器,将请求参数转换为handle方法的函数参数
             * @param registry
             */
            @Override
            public void addFormatters(FormatterRegistry registry) {
                registry.addConverter((Converter<String, LocalDate>) source -> LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                registry.addConverter((Converter<String, LocalTime>) source -> LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm")));
            }

            /**
             * 自定义拦截器
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
            }


        };
    }

}
