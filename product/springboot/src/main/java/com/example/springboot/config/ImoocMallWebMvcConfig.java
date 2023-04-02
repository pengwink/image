package com.example.springboot.config;
import com.example.springboot.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 描述：     配置地址映射
 */
@Configuration
public class ImoocMallWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceLocations 指的是文件放置的目录，addResoureHandler 指的是对外暴露的访问路径
        Constant Constant = null;
        registry.addResourceHandler("/file/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);
        //registry.addResourceHandler("swagger-ui.html").addResourceLocations(
        //       "classpath:/META-INF/resources/");
        //registry.addResourceHandler("/webjars/**").addResourceLocations(
        //        "classpath:/META-INF/resources/webjars/");
    }
}
