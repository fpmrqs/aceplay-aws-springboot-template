package tech.makers.aceplay.session;

import tech.makers.aceplay.user.User;

public class Session {
  private User user;
  private String token;

  public Session(User user, String token) {
    this.setUser(user);
    this.setToken(token);
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
