package tech.makers.aceplay.storage;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

@Service
public class AwsStorageService {
  private static final String[] ACCEPTABLE_FILE_TYPES = new String[] { "audio/mpeg", "audio/x-aiff", "audio/wave", "audio/wav", "audio/vnd.wav", "audio/ogg", "audio/vorbis" };
  private static final String AWS_URL_SUFFIX = "amazonaws.com";

  @Autowired private AmazonS3 s3Client;

  @Value("${aws.bucketName}")
  private String awsBucketName;

  @Value("${aws.region}")
  private String awsRegion;

  public URL makeSignedUploadUrl(String filename, String contentType)
      throws IOException, InvalidProposedMimeType {

    // Check that the provided content type matches our list of acceptable formats
    validateUploadContentType(contentType);

    // Set the presigned URL to expire after one hour from now.
    Date expiration = new Date();
    long expTimeMillis = Instant.now().toEpochMilli() + (1000 * 60 * 60);
    expiration.setTime(expTimeMillis);

    // Generate the presigned URL.
    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(awsBucketName, filename, HttpMethod.PUT)
        .withExpiration(expiration);

    // Set the content type header on the file to match the file we received
    generatePresignedUrlRequest.setContentType(contentType);

    return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
  }

  public URL getPublicUrl(String filename) throws MalformedURLException {
    return new URL(String.format("https://%s.s3.%s.%s/%s", awsBucketName, awsRegion, AWS_URL_SUFFIX, filename));
  }

  private void validateUploadContentType(String contentType) throws InvalidProposedMimeType {
    if (!Arrays.stream(ACCEPTABLE_FILE_TYPES).anyMatch(contentType::equals)) {
      throw new InvalidProposedMimeType(contentType, "Only audio files are supported");
    }
  }
}
