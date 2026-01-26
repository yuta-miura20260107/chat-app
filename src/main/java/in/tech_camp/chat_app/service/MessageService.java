package in.tech_camp.chat_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.MessageEntity;
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.MessageForm;
import in.tech_camp.chat_app.repository.MessageRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;

  public boolean postMessage(MessageForm messageForm, CustomUserDetail currentUser, Integer roomId) {
    
    UserEntity user = new UserEntity();
    user.setId(currentUser.getId());

    RoomEntity room = new RoomEntity();
    room.setId(roomId);

    var message = new MessageEntity();
    message.setContent(messageForm.getContent());
    message.setUser(user);
    message.setRoom(room);


    messageRepository.insert(message);

    return true;
  }

  public List<MessageEntity> getRoomsMessage(Integer roomId) {
    List<MessageEntity> messages = messageRepository.findByRoomId(roomId);
    return messages;
  }
}
