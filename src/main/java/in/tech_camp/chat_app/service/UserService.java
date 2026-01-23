package in.tech_camp.chat_app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.UserEditForm;
import in.tech_camp.chat_app.form.UserForm;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void registerUser(UserForm userForm) {
    String encodedPassword = passwordEncoder.encode(userForm.getPassword());
    UserEntity user = UserEntity.builder()
        .name(userForm.getName())
        .email(userForm.getEmail())
        .password(encodedPassword)
        .build();

    userRepository.insert(user);
  }

  public UserEditForm getUserEditInitialData(CustomUserDetail user) {
    UserEntity latestUserEntity = userRepository.findById(user.getId());
    UserEditForm form = new UserEditForm();
    form.setId(latestUserEntity.getId());
    form.setName(latestUserEntity.getName());
    form.setEmail(latestUserEntity.getEmail());
    return form;
  }

  public void updateUser(UserEditForm userEditForm) {
    UserEntity user = UserEntity.builder()
        .id(userEditForm.getId())
        .email(userEditForm.getEmail())
        .name(userEditForm.getName())
        .build();

    userRepository.update(user);
  }
}
