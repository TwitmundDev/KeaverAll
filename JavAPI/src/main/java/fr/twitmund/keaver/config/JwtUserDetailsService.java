package fr.twitmund.keaver.config;

import fr.twitmund.keaver.db.entities.UserEntity;
import fr.twitmund.keaver.db.model.JwtUserDetails;
import fr.twitmund.keaver.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).get();

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new JwtUserDetails(user);

    }

}
