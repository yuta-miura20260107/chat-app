package in.tech_camp.chat_app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.MessageEntity;

@Mapper
public interface MessageRepository {

  @Insert("INSERT INTO messages (content, room_id, user_id, image) VALUES (#{content}, #{room.id}, #{user.id}, #{image})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  public void insert(MessageEntity message);

  @Select("SELECT " 
    + "m.id, m.content, m.image, m.created_at, "
    + "u.id AS user_id, u.name AS user_name "
    + "FROM messages m "
    + "LEFT OUTER JOIN rooms r ON r.id = m.room_id "
    + "LEFT OUTER JOIN users u ON u.id = m.user_id "
    + "WHERE m.room_id = #{roomId} "
    + "ORDER BY m.created_at ASC")
  @Results(value = {
    @Result(property = "id", column = "id"),
    @Result(property = "content", column = "content"),
    @Result(property = "image", column = "image"),
    @Result(property = "created_at", column = "created_at"),
    @Result(property = "user.id", column = "user_id"),
    @Result(property = "user.name", column = "user_name") 
  })
  public List<MessageEntity> findByRoomId(Integer roomId);
}
