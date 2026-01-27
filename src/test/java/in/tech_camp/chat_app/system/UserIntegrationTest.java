package in.tech_camp.chat_app.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import in.tech_camp.chat_app.ChatAppApplication;
import in.tech_camp.chat_app.factories.UserFormFactory;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.service.UserService;

@ActiveProfiles("test")
@SpringBootTest(classes = ChatAppApplication.class)
@AutoConfigureMockMvc
public class UserIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private UserService userService;

  private UserForm userForm;

  @Test
  public void ログインしていない状態でトップページにアクセスした場合サインインページに移動する() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("http://localhost/users/login"));
  }

  @Test
  public void ログインに成功しトップページに遷移する() throws Exception {
    userForm = UserFormFactory.createUser();
    userService.registerUser(userForm);
    
    mockMvc.perform(get("/users/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("users/login"));

    var loginResult = mockMvc.perform(post("/login")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .param("email", userForm.getEmail())
          .param("password", userForm.getPassword())
          .with(csrf()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"))
        .andReturn();

    mockMvc.perform(get("/").session((MockHttpSession)loginResult.getRequest().getSession()))
            .andExpect(status().isOk())
            .andExpect(view().name("rooms/index"));
  }

  @Test
  public void ログインに失敗し再びサインインページに戻ってくる() throws Exception {
    // // 予め、ユーザーをDBに保存する
    userForm = UserFormFactory.createUser();
    // userService.registerUser(userForm);

    // サインインページに遷移する
    // 誤ったユーザーでログインするとエラーパスにリダイレクトされる
    mockMvc.perform(post("/login")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .param("email", userForm.getEmail())
          .param("password", userForm.getPassword())
          .with(csrf()))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/login?error"));

    // エラーパスにリダイレクトされたとき、サインインのビューが表示される
    mockMvc.perform(get("/login?error").param("error", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("users/login"));
  }
}