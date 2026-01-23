package in.tech_camp.chat_app.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {
  private Integer id;
  private String name;
  private String email;
  private String password;
}
