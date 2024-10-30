package kr.re.kh;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        KeitiApplication.class,
        Jsr310JpaConverters.class
})
public class KeitiApplication {

    @Value("${upload.path}")
    private String uploadPath;

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(KeitiApplication.class, args);
    }

    @Bean(name = "uploadPath")
    public String uploadPath() {
        return uploadPath;
    }

}
