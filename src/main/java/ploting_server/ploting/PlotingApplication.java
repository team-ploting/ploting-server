package ploting_server.ploting;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class PlotingApplication {

    public static void main(String[] args) {
        initEnv();
        SpringApplication.run(PlotingApplication.class, args);
    }

    /**
     * 스트링 부트 실행 전 시스템 속성(System Properties)을 설정한다.
     */
    static void initEnv() {
        Dotenv.configure()
                .directory("./")
                .filename(".env")
                .load()
                .entries()
                .forEach(e -> {
                    System.setProperty(e.getKey(), e.getValue());
                });
    }
}
