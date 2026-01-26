package in.tech_camp.chat_app.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.MessageEntity;
import in.tech_camp.chat_app.form.MessageForm;
import in.tech_camp.chat_app.service.MessageService;
import in.tech_camp.chat_app.service.RoomService;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class MessageController {

  private final RoomService roomService;
  private final MessageService messageService;

  @GetMapping("/message")
  public String showRootMessages(
      @AuthenticationPrincipal CustomUserDetail user,
      @ModelAttribute("messageForm") MessageForm messageForm,
      Model model
  ){
    var rooms = roomService.getUserRooms(user);
    model.addAttribute("rooms", rooms);
    model.addAttribute("currentUserName", user.getName());


    return "messages/index";
  }
  
  @GetMapping("/rooms/{roomId}/messages")
  public String showRoomMessagesById(
      @AuthenticationPrincipal CustomUserDetail user,
      @PathVariable("roomId") Integer roomId,
      @ModelAttribute("messageForm") MessageForm messageForm,
      Model model
  ){

    var rooms = roomService.getUserRooms(user);

    var currentRoom = rooms.stream()
        .filter(r -> r.getId().equals(roomId))
        .findAny();

    // 部屋IDがDBに存在しなければ終了
    if(currentRoom.isEmpty()) {
      return "redirect:/";
    }

    model.addAttribute("rooms", rooms);
    model.addAttribute("currentRoom", currentRoom.get());
    model.addAttribute("currentUserName", user.getName());

    List<MessageEntity> messages = messageService.getRoomsMessage(roomId);
    model.addAttribute("messages", messages);

    return "messages/index";
  }

  @PostMapping("/rooms/{roomId}/messages")
  public String postMessage(
      @AuthenticationPrincipal CustomUserDetail user,
      @PathVariable("roomId") Integer roomId,
      @ModelAttribute("messageForm") MessageForm messageForm,
      Model model
  ) {
    
    messageService.postMessage(messageForm, user, roomId);
    
    return "redirect:/rooms/" + roomId + "/messages";
  }
  
}