package tech.makers.aceplay.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidProposedMimeTypeTest {

  @Test
  void testGetMessage() {
    InvalidProposedMimeType subject = new InvalidProposedMimeType("text/plain", "message");
    assertEquals("Invalid mime type \"text/plain\": message", subject.getMessage());
  }
}
