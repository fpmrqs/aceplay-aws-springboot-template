package tech.makers.aceplay.storage;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@RestController
public class StorageController {
  @Autowired private AwsStorageService awsStorageService;

  @GetMapping("/api/storage/upload_key")
  public TrackUploadKeyDto getTrackUploadKey(
      @RequestParam String filename, @RequestParam String contentType)
      throws IOException, InvalidProposedMimeType {
    String uploadedFilename = makeUniqueFilename(filename);
    URL signedUploadUrl = awsStorageService.makeSignedUploadUrl(uploadedFilename, contentType);
    URL publicUrl = awsStorageService.getPublicUrl(uploadedFilename);
    return new TrackUploadKeyDto(publicUrl, signedUploadUrl);
  }

  private String makeUniqueFilename(String filename) {
    String extension = FilenameUtils.getExtension(filename);
    String basename = FilenameUtils.removeExtension(filename);
    String uuid = UUID.randomUUID().toString();
    return String.format("%s-%s.%s", basename, uuid, extension);
  }
}
