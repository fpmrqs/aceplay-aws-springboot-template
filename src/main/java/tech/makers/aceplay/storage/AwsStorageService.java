package tech.makers.aceplay.storage;

import org.apache.tika.*;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

// https://www.youtube.com/watch?v=vreyOZxdb5Y&t=1084s
@Service
public class AwsStorageService {
  private static final String[] ACCEPTABLE_FILE_TYPES = new String[] { "audio/mpeg", "audio/x-aiff", "audio/wave", "audio/wav", "audio/vnd.wav", "audio/ogg", "audio/vorbis" };
  private static final String STORAGE_URL = "s3.eu-west-2.amazonaws.com";

  @Autowired private AmazonS3 s3Client;

  @Value("${aws.bucketName}")
  private String awsBucketName;

  public URL makeSignedUploadUrl(String filename, String contentType)
      throws IOException, InvalidProposedMimeType {

    // Check that the provided content type matches our list of acceptable formats
    validateUploadContentType(contentType);

    // Set s3 Properties
    String bucketName = awsBucketName;
    String objectKey = filename;

    // Set the presigned URL to expire after one hour.
    java.util.Date expiration = new java.util.Date();
    long expTimeMillis = Instant.now().toEpochMilli();
    expTimeMillis += 1000 * 60 * 60;
    expiration.setTime(expTimeMillis);

    System.out.println(awsBucketName);

    // Generate the presigned URL.
    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey, HttpMethod.PUT)
        .withExpiration(expiration);

    // Set the content type header on the file
    generatePresignedUrlRequest.setContentType(contentType);

    return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
  }

  public URL getPublicUrl(String filename) throws MalformedURLException {
    return new URL(String.format("https://%s.%s/%s", awsBucketName, STORAGE_URL, filename));
  }

  private void validateUploadContentType(String contentType) throws InvalidProposedMimeType {
    if (!Arrays.stream(ACCEPTABLE_FILE_TYPES).anyMatch(contentType::equals)) {
      throw new InvalidProposedMimeType(contentType, "Only audio files are supported");
    }
  }
}
