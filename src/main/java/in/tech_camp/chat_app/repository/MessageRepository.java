package in.tech_camp.chat_app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import in.tech_camp.chat_app.entity.MessageEntity;

@Mapper
public interface MessageRepository {

  @Insert("INSERT INTO messages (content, room_id, user_id, image) VALUES (#{content}, #{room.id}, #{user.id}, #{image})")
  public void insert(MessageEntity message);
}
