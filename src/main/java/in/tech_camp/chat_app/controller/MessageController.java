package in.tech_camp.chat_app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.service.RoomService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MessageController {

  private RoomService roomService;

  @GetMapping("/message")
  public String showMessages(
      @AuthenticationPrincipal CustomUserDetail user,
      Model model
  ){
    var rooms = roomService.getUserRooms(user);
    model.addAttribute("rooms", rooms);
    model.addAttribute("currentUserName", user.getName());

    return "messages/index";
  }
}