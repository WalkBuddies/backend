package com.walkbuddies.backend.common;

import com.walkbuddies.backend.common.domain.FileEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
  @Bean
  public FileEntity fileEntity() {
    return new FileEntity();
  }

}
