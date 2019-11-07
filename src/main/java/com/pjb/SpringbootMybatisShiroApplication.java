package com.pjb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
@MapperScan("com.pjb.mapper")
//public class SpringbootMybatisShiroApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(SpringbootMybatisShiroApplication.class, args);
//	}
//}
/*
    Application文件路径发生变化，要重新配置Bean组件的扫描信息
 */
public class SpringbootMybatisShiroApplication extends WebMvcConfigurationSupport {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisShiroApplication.class, args);
	}

	//这里配置静态资源文件的路径导包都是默认的直接导入就可以
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
		super.addResourceHandlers(registry);
	}
}
