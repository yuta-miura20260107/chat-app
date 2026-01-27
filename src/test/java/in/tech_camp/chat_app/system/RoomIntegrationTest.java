package in.tech_camp.chat_app.system;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import in.tech_camp.chat_app.ChatAppApplication;
import in.tech_camp.chat_app.ImageUrl;
import in.tech_camp.chat_app.WebConfig;
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.RoomUserEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.factories.MessageFormFactory;
import in.tech_camp.chat_app.factories.RoomFormFactory;
import in.tech_camp.chat_app.factories.UserFormFactory;
import in.tech_camp.chat_app.form.MessageForm;
import in.tech_camp.chat_app.form.RoomForm;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.repository.MessageRepository;
import in.tech_camp.chat_app.repository.RoomRepository;
import in.tech_camp.chat_app.repository.RoomUsersRepository;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.service.MessageService;
import in.tech_camp.chat_app.service.UserService;
import in.tech_camp.chat_app.support.LoginSupport;

@ActiveProfiles("test")
@SpringBootTest(classes = ChatAppApplication.class)
@AutoConfigureMockMvc
@Import(WebConfig.class)
public class RoomIntegrationTest {
  private UserForm userForm1;
  private UserForm userForm2;
  private RoomForm roomForm;
  private MessageForm messageForm;

  private UserEntity userEntity1;
  private UserEntity userEntity2;
  private RoomEntity roomEntity;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ImageUrl imageUrl;

  @Autowired
  private UserService userService;
  @Autowired
  private MessageService messageService;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private RoomUsersRepository roomUserRepository;

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setup() {
    userForm1 = UserFormFactory.createUser();
    userService.registerUser(userForm1);
    userEntity1 = userRepository.findByEmail(userForm1.getEmail());

    userForm2 = UserFormFactory.createUser();
    userService.registerUser(userForm2);
    userEntity2 = userRepository.findByEmail(userForm2.getEmail());

    roomForm = RoomFormFactory.createRoom();
    roomEntity = new RoomEntity();
    roomEntity.setName(roomForm.getName());
    roomRepository.insert(roomEntity);

    RoomUserEntity roomUserEntity = new RoomUserEntity();
    roomUserEntity.setRoom(roomEntity);
    roomUserEntity.setUser(userEntity1);
    roomUserRepository.insert(roomUserEntity);
    roomUserEntity.setRoom(roomEntity);
    roomUserEntity.setUser(userEntity2);
    roomUserRepository.insert(roomUserEntity);

    messageForm = MessageFormFactory.createMessage();
  }

  
  @AfterEach
  public void cleanup() throws IOException {
    Path directoryPath = Paths.get(imageUrl.getImageUrl());

    // ディレクトリが存在することを確認
    if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
      // ディレクトリ内のすべてのファイルを削除
      try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
        for (Path filePath : directoryStream) {
          Files.deleteIfExists(filePath);
        }
      }
    }
  }

  @Test
  public void チャットルームを削除すると関連するメッセージがすべて削除されている() throws Exception {
    // サインインする
    var session = LoginSupport.login(mockMvc, userForm1);

    // 作成されたチャットルームへ遷移する
    mockMvc.perform(get("/rooms/{roomId}/messages", roomEntity.getId()).session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("messages/index"));

    long initialCount = messageRepository.count();
    // メッセージ情報を5つDBに追加する
    for(int i = 0; i < 5; i++) {
      messageForm = MessageFormFactory.createMessage();
      messageService.postMessage(messageForm, userEntity1.getId(), roomEntity.getId());
    }
    int messagePostCount =  messageRepository.count();
    assertEquals(5, messagePostCount);

    // チャットルームを削除するとトップページに遷移することを確認する
    mockMvc.perform(post("/rooms/{roomId}/delete", roomEntity.getId()).session(session))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/"));

    // 作成した5つのメッセージが削除されていることを確認する
    int messageCount =  messageRepository.count();
    assertEquals(0, messageCount);

  }
}