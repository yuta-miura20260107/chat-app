package in.tech_camp.chat_app.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;

import in.tech_camp.chat_app.factories.UserFormFactory;
import in.tech_camp.chat_app.validation.ValidationOrder;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
@SpringBootTest
public class UserFormUnitTest {

  private UserForm userForm;
  private Validator validator;
  private BindingResult bindingResult;

  @BeforeEach
  public void setUp() {
    userForm = UserFormFactory.createUser();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

    bindingResult = Mockito.mock(BindingResult.class);
  }

  @Nested
  class ユーザーを作成できる場合 {
    @Test
    public void nameとemailとpasswordとpasswordconfirmationが存在すれば登録できる () {
      var violations = validator.validate(userForm);
      assertEquals(0, violations.size());
    } 
  }

  @Nested
  class ユーザーを作成できない場合 {
    @Test
    public void nameが空では登録できない () {
      userForm.setName("");
      var violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }

    @Test
    public void emailが空では登録できない () {
      userForm.setEmail("");
      var violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }

    @Test
    public void passwordが空では登録できない() {
      userForm.setPassword("");
      var violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }
    
    @Test
    public void emailはアットマークを含まないと登録できない() {
      var violateEmail = userForm.getEmail().replaceAll("@", "");
      userForm.setEmail(violateEmail);
      var violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }
    
    @Test
    public void passwordが5文字以下では登録できない() {
      var violatePassword = "a".repeat(5);
      userForm.setPassword(violatePassword);
      var violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }

    @Test
    public void passwordが129文字以上では登録できない() {
      var violatePassword = "a".repeat(129);
      userForm.setPassword(violatePassword);
      var violations = validator.validate(userForm, ValidationOrder.class);
      assertEquals(1, violations.size());
    }

    @Test
    public void passwordとpasswordConfirmationが不一致では登録できない() {
      userForm.setPassword("password");
      userForm.setPasswordConfirmation("ignorePass");
      userForm.validatePasswordConfirmation(bindingResult);
      verify(bindingResult).rejectValue("passwordConfirmation", "error.user", "Password confirmation doesn't match Password");
    }
  }
}