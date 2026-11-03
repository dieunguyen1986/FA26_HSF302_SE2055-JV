package fu.hr.service;

import fu.hr.entity.User;
import fu.hr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(username);

        User user = optionalUser.orElseThrow(() -> {
            return new RuntimeException("User not found!");
        });
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user.getRole() != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails
                .User(user.getEmail(), user.getPasswordHash(), grantedAuthorities);


        return userDetails;
    }
}
