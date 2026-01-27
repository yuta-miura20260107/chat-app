package in.tech_camp.chat_app.service;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.chat_app.ImageUrl;
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
  private final ImageUrl imageUrl;

  public boolean postMessage(MessageForm messageForm, Integer userId, Integer roomId) throws IOException {
    

    UserEntity user = new UserEntity();
    user.setId(userId);

    RoomEntity room = new RoomEntity();
    room.setId(roomId);

    var message = new MessageEntity();
    message.setContent(messageForm.getContent());
    message.setUser(user);
    message.setRoom(room);

    /* ここからコピペ */
    MultipartFile imageFile = messageForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      for(int i = 0; i < 100; i++) {
        try {
          String uploadDir = imageUrl.getImageUrl();
          String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + i + "_" + imageFile.getOriginalFilename();
          Path imagePath = Paths.get(uploadDir, fileName);
          Files.copy(imageFile.getInputStream(), imagePath);
          message.setImage("/uploads/" + fileName);

        } catch (FileAlreadyExistsException exception) {
          continue;
          
        } catch (IOException e) {
          System.err.println("エラー");
          System.err.println(e.getMessage());
          Arrays.stream(e.getStackTrace())
              .forEach(stack -> System.out.println(stack.toString()));
          throw e;
        }
      }
    }
    /* ここまでコピペ */

    messageRepository.insert(message);

    return true;
  }

  public List<MessageEntity> getRoomsMessage(Integer roomId) {
    List<MessageEntity> messages = messageRepository.findByRoomId(roomId);
    return messages;
  }
}
