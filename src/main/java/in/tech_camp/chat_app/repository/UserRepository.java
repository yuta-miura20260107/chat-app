package in.tech_camp.chat_app.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.UserEntity;

@Mapper
public interface UserRepository {

  @Insert("INSERT INTO users(name, email, password) VALUES (#{name}, #{email}, #{password})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  public void insert(UserEntity userEntity);

  @Select("SELECT id, name, email, password FROM users WHERE id = #{id} LIMIT 1")
  public UserEntity findById(Integer id);
  
  @Select("SELECT id, name, email, password FROM users WHERE email = #{email} LIMIT 1")
  public UserEntity findByEmail(String email);
}
