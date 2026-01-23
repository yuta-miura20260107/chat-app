package in.tech_camp.chat_app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
@Setter
public class UserEntity {
  private Integer id;
  private String name;
  private String email;
  private String password;
}
