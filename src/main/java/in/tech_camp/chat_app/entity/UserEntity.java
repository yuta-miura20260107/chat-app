package in.tech_camp.chat_app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEntity {
  private final Integer id;
  private final String name;
  private final String email;
  private final String password;
}
