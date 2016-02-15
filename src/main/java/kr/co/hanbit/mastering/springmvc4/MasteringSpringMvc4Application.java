package kr.co.hanbit.mastering.springmvc4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import kr.co.hanbit.mastering.springmvc4.config.PicturesUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({ PicturesUploadProperties.class })
public class MasteringSpringMvc4Application {

	public static void main(String[] args) {
		SpringApplication.run(MasteringSpringMvc4Application.class, args);
	}
}
