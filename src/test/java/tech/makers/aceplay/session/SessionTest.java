package tech.makers.aceplay.session;

import org.junit.jupiter.api.Test;
import tech.makers.aceplay.user.User;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {
  @Test
  void testConstructs() {
    User user = new User("username", "password");
    Session subject = new Session(user, "token");
    assertEquals(user, subject.getUser());
    assertEquals("token", subject.getToken());
  }
}
