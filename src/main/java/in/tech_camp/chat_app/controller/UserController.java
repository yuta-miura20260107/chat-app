package in.tech_camp.chat_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.chat_app.form.LoginForm;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.service.UserService;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  
  @GetMapping("/users/sign_up")
  public String signUp(
      Model model
  ) {
    model.addAttribute("userForm", new UserForm());
    return "users/signUp";
  }
  
  @GetMapping("/login")
  public String loginWithError(
    @RequestParam(value = "error") String error,
      @ModelAttribute("loginForm") LoginForm loginForm,
      Model model
  ) {
    if(error != null) {
      model.addAttribute("loginError", "メールアドレスまたはパスワードが違います");
    }
    return "users/login";
  }

  @GetMapping("/users/login")
  public String login(
      @ModelAttribute("loginForm") LoginForm loginForm
  ) {
    return "users/login";
  }

  @PostMapping("/user")
  public String postMethodName(
      @ModelAttribute("userForm") @Validated UserForm userForm,
      BindingResult result
  ) {
    
    // TODO: バリデーション処理を入れる
    // TODO: ユーザーの登録済み確認

    userService.registerUser(userForm);
    
    return "redirect:/";
  }
}
