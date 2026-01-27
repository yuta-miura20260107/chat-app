package in.tech_camp.chat_app.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;

import in.tech_camp.chat_app.factories.MessageFormFactory;

@ActiveProfiles("test")
@SpringBootTest
public class MessageFormUnitTest {
  private MessageForm messageForm;
  private BindingResult bindingResult;

  @BeforeEach
  public void setUp() {
    messageForm = MessageFormFactory.createMessage();
    bindingResult = Mockito.mock(BindingResult.class);
  }

  @Nested
  class メッセージが投稿できる場合 {
    @Test
    public void contentとimageが存在していれば保存できる () {

    }

    @Test
    public void contentが空でも保存できる () {

    }

    @Test
    public void contentがnullでも保存できる () {

    }

    @Test
    public void imageが空でも保存できる () {

    }

    @Test
    public void imageがnullでも保存できる () {

    }
  }

  @Nested
  class メッセージが投稿できない場合 {
    @Test
    public void contentが空かつimageが空ファイルだと保存できない() {

    }

    @Test
    public void contentが空かつimageがnullだと保存できない() {

    }

    @Test
    public void contentがnullかつimageが空ファイルだと保存できない() {

    }

    @Test
    public void contentがnullかつimageがnullだと保存できない() {

    }
  }
}