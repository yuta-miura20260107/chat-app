package in.tech_camp.chat_app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.form.LoginForm;
import in.tech_camp.chat_app.form.UserEditForm;
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
  public String registerUser(
      @ModelAttribute("userForm") @Validated UserForm userForm,
      BindingResult result,
      Model model
  ) {
    
    if(result.hasErrors()) {
      model.addAttribute("errorMessages", result.getAllErrors());
      
      UserForm form = new UserForm();
      model.addAttribute("userForm", form);
      return "users/sign_up";
    }

    userService.registerUser(userForm);
    
    return "redirect:/";
  }
  
  @GetMapping("/users/edit")
  public String postMethodName(
      @AuthenticationPrincipal CustomUserDetail currentUser,
      Model model
  ) {
    UserEditForm form = userService.getUserEditInitialData(currentUser);
    model.addAttribute("user", form);
    return "users/edit";
  }

  @PostMapping("/users/{id}")
  public String updateUser(
      @AuthenticationPrincipal CustomUserDetail currentUser,
      @ModelAttribute("user") @Validated UserEditForm userEditForm,
      BindingResult result, 
      Model model
  ) {
    // IDが改竄
    if(!userEditForm.getId().equals(currentUser.getId())) {
      model.addAttribute("error", "不正なリクエストです。");
      UserEditForm form = userService.getUserEditInitialData(currentUser);
      model.addAttribute("user", form);
      return "users/edit";
    }

    // エラーを取得
    if(result.hasErrors()){
      model.addAttribute("errorMessages", result.getAllErrors());
      UserEditForm form = userService.getUserEditInitialData(currentUser);
      model.addAttribute("user", form);
      return "users/edit";
    }

    userService.updateUser(userEditForm);
    return "redirect:/";
  }
}
