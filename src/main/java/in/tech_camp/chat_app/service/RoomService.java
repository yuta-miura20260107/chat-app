package in.tech_camp.chat_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.RoomUserEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.RoomForm;
import in.tech_camp.chat_app.repository.RoomRepository;
import in.tech_camp.chat_app.repository.RoomUsersRepository;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final RoomUsersRepository roomUsersRepository;

  public void createRoom(RoomForm roomForm) {
    RoomEntity roomEntity = new RoomEntity();
    roomEntity.setName(roomForm.getName());
    try {
      roomRepository.insert(roomEntity);
    } catch (Exception e) {
      throw e;
    }

    List<Integer> memberIds = roomForm.getMemberIds();
    for (Integer userId : memberIds) {
      UserEntity userEntity = userRepository.findById(userId);
      RoomUserEntity roomUserEntity = new RoomUserEntity();
      roomUserEntity.setRoom(roomEntity);
      roomUserEntity.setUser(userEntity);
      try {
        roomUsersRepository.insert(roomUserEntity);
      } catch (Exception e) {
        /* 
        System.out.println("エラー：" + e);
        List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
        model.addAttribute("users", users);
        model.addAttribute("roomForm", new RoomForm());
        return "rooms/new";
        */
        throw e;
      }
    }
  }

  public List<RoomEntity> getUserRooms(CustomUserDetail user) {
    List<RoomUserEntity> roomUsers = roomUsersRepository.findByUserId(user.getId());
    
    var rooms = roomUsers.stream()
        .map(r -> r.getRoom())
        .toList();

    return rooms;
  }
}
