package in.tech_camp.chat_app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.custom_user.LoginUser;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
    UserEntity user = userRepository.findByEmail(username);

    if(user == null){
      throw new UsernameNotFoundException("ユーザー名が存在しません。");
    }

    return new CustomUserDetail(new LoginUser(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getPassword()
    ));
  }
}
