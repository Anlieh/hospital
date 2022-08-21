package ycw.hospital.hosp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("ycw.hospital.mapper")
public class HospitalConfig {
}
