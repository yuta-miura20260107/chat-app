package in.tech_camp.chat_app.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.chat_app.factories.RoomFormFactory;
import in.tech_camp.chat_app.validation.ValidationOrder;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
@SpringBootTest
public class RoomFormUnitTest {
  private RoomForm roomForm;

  private Validator validator;

  @BeforeEach
  public void setUp() {
    roomForm = RoomFormFactory.createRoom();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Nested
  class ルームを作成できる場合 {
    @Test
    public void nameの値が存在すれば作成できる () {
      var violations = validator.validate(roomForm, ValidationOrder.class);
      assertTrue(violations.isEmpty());
    }
  }

  @Nested
  class ルームを作成できない場合 {
    @Test
    public void nameが空では作成できない () {
      roomForm.setName("");
      var violations = validator.validate(roomForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }
  }
}