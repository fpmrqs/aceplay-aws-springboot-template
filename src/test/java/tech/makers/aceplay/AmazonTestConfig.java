package tech.makers.aceplay;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.amazonaws.services.s3.AmazonS3;

@TestConfiguration
public class AmazonTestConfig {
  @MockBean
  AmazonS3 s3;
}
