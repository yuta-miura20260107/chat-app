package in.tech_camp.chat_app.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.chat_app.factories.UserEditFormFactory;
import in.tech_camp.chat_app.validation.ValidationOrder;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
@SpringBootTest
public class UserEditFormUnitTest {

  private UserEditForm userEditForm;
  private Validator validator;

  @BeforeEach
  public void setUp() {
    userEditForm = UserEditFormFactory.createEditUser();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Nested
  class ユーザー情報を編集できる場合 {
    @Test
    public void 全てデータが存在すれば保存できること () {
      var violations = validator.validate(userEditForm, ValidationOrder.class);
      assertTrue(violations.isEmpty());
    }
  }

  @Nested
  class ユーザー情報を編集できない場合 {
    @ParameterizedTest
    @NullAndEmptySource  
    public void nameが空では登録できない (String name) {
      userEditForm.setName(name);
      var violations = validator.validate(userEditForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }

    @ParameterizedTest
    @NullAndEmptySource  
    public void emailが空では登録できない (String email) {
      userEditForm.setEmail(email);
      var violations = validator.validate(userEditForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }

    @Test
    public void emailは無効なメールでは登録できない() {
      var violateEmail = userEditForm.getEmail().replace("@", "");
      userEditForm.setEmail(violateEmail);
      var violations = validator.validate(userEditForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }
  }
}