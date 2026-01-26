package in.tech_camp.chat_app.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.RoomForm;
import in.tech_camp.chat_app.service.RoomService;
import in.tech_camp.chat_app.service.UserService;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class RoomController {

  private final UserService userService;
  private final RoomService roomService;

  @GetMapping("/")
  public String showMessages(
      @AuthenticationPrincipal CustomUserDetail user,
      Model model
  ){
    var rooms = roomService.getUserRooms(user);
    model.addAttribute("rooms", rooms);
    model.addAttribute("currentUserName", user.getName());

    return "rooms/index";
  }

  @GetMapping("/rooms/new")
  public String shownNewRoom(
      @AuthenticationPrincipal CustomUserDetail currentUser,
      Model model
  ) {

    List<UserEntity> users = userService.getSelectUsers(currentUser.getId());
    model.addAttribute("users", users);
    model.addAttribute("roomForm", new RoomForm());

    return "rooms/new";
  }
  
  @PostMapping("/rooms")
  public String createRoom(
      @ModelAttribute("RoomForm") RoomForm roomForm
  ){
    System.out.println("roomForm:"+ roomForm);
    roomService.createRoom(roomForm);
    return "redirect:/";
  }
}
