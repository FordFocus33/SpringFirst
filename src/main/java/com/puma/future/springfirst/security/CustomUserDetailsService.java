package com.puma.future.springfirst.security;

import com.puma.future.springfirst.model.Roles;
import com.puma.future.springfirst.model.UserEntity;
import com.puma.future.springfirst.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Находим нашего юзера по юзернейму и пеоедаем его данные спринговому юзеру
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Нет юзера с таким именем"));

        return new User(user.getUsername(),
                        user.getPassword(),
                        mapRolesToAuthorities(user.getRoles())
        );
    }

    // Приводим нашу стринговую роль в GrantedAuthorities, т.к. Спринговый User должен 3-м параметром получать роль унаследованную от GrantedAuthorities
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Roles> rolesList){
        return rolesList.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
