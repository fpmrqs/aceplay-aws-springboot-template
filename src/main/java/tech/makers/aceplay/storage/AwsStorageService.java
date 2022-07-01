package tech.makers.aceplay.storage;

import org.apache.tika.*;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
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
  private static final String[] ACCEPTABLE_FILE_TYPES = new String[] { "audio/mpeg", "audio/x-aiff", "audio/vnd.wav", "audio/ogg", "audio/vorbis" };
  private static final String STORAGE_URL = "http://s3.amazonaws.com/";

  @Value("${aws.bucketName}")
  private String awsBucketName;

  public void upload(String path,
      String fileName,
      Optional<Map<String, String>> optionalMetaData,
      InputStream inputStream) throws IOException {

    ObjectMetadata objectMetadata = new ObjectMetadata();
    optionalMetaData.ifPresent(map -> {
      if (!map.isEmpty()) {
        map.forEach(objectMetadata::addUserMetadata);
      }
    });
    try {
      AmazonS3 s3Client = makeClient();
      s3Client.putObject(path, fileName, inputStream, objectMetadata);
    } catch (AmazonServiceException e) {
      throw new IllegalStateException("Failed to upload the file", e);
    }
  }

  public URL makeSignedUploadUrl(String filename, String contentType)
      throws IOException, InvalidProposedMimeType {
    validateUploadContentType(contentType);
    // AmazonS3 s3Client = makeClient();
    // String bucketName = awsBucketName;
    // String objectKey = filename;

    // // Set the presigned URL to expire after one hour.
    // java.util.Date expiration = new java.util.Date();
    // long expTimeMillis = Instant.now().toEpochMilli();
    // expTimeMillis += 1000 * 60 * 60;
    // expiration.setTime(expTimeMillis);

    // // Generate the presigned URL.
    // GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
    //     .withMethod(HttpMethod.GET)
    //     .withExpiration(expiration);

    // return s3Client.generatePresignedUrl(generatePresignedUrlRequest);

    return new URL("http", "test.com", "octopus/test");

  }

  // public URL makeSignedUploadUrl(String filename, String contentType)
  // throws IOException, InvalidProposedMimeType {
  // validateUploadContentType(contentType);

  // Storage storage = makeStorage();
  // BlobInfo blobInfo = makeBlobInfo(filename);

  // Map<String, String> extensionHeaders = makeHeaders(contentType);
  // return storage.signUrl(
  // blobInfo,
  // 15,
  // TimeUnit.MINUTES,
  // Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
  // Storage.SignUrlOption.withExtHeaders(extensionHeaders),
  // Storage.SignUrlOption.withV4Signature());
  // }

  public URL getPublicUrl(String filename) throws MalformedURLException {
    return new URL(STORAGE_URL + awsBucketName + "/" + filename);
  }

  private Map<String, String> makeHeaders(String contentType) {
    Map<String, String> extensionHeaders = new HashMap<>();
    extensionHeaders.put("Content-Type", contentType);
    return extensionHeaders;
  }

  private AmazonS3 makeClient() throws IOException {
    Regions clientRegion = Regions.EU_WEST_2;
    return AmazonS3ClientBuilder.standard()
        .withRegion(clientRegion)
        .withCredentials(new ProfileCredentialsProvider())
        .build();
  }

  private void validateUploadContentType(String contentType) throws InvalidProposedMimeType {
    if (!Arrays.stream(ACCEPTABLE_FILE_TYPES).anyMatch(contentType::equals)) {
      throw new InvalidProposedMimeType(contentType, "Only audio files are supported");
    }
  }
}
