package in.tech_camp.chat_app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;

@Controller
public class MessageController {
  @GetMapping("/")
  public String showMessages(
      @AuthenticationPrincipal CustomUserDetail user,
      Model model
  ){
    model.addAttribute("currentUserName", user.getName());
    return "messages/index";
  }
}