package com.treabear.readinglist;

import java.util.List;

import com.treabear.readinglist.resolver.ReaderHandlerMethodArgumentResolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication	// 컴포넌트 검색과 자동 구성 활성화
public class ReadingListApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		// 애플리케이션 부트스트랩
		SpringApplication.run(ReadingListApplication.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// /login 경로를 login 템플릿으로 매핑
		registry.addViewController("/login").setViewName("login");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// Reader 타입의 객체가 컨트롤러 매개변수로 있을 때 처리할 리졸버 설정
		argumentResolvers.add(new ReaderHandlerMethodArgumentResolver());
	}
}
