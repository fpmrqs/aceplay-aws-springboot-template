package tech.makers.aceplay.playlist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrackIdentifierDtoTest {
  @Test
  void testConstructs() {
    TrackIdentifierDto subject = new TrackIdentifierDto(5L);
    assertEquals(5L, subject.getId());
  }
}
