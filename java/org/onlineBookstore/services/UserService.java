package org.onlineBookstore.services;


import org.onlineBookstore.entities.Role;
import org.onlineBookstore.entities.User;
import org.onlineBookstore.repo.RoleRepository;
import org.onlineBookstore.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private BasketService basketService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean findByUserName(String userName) {
        if (userRepository.findByUsername(userName) != null) {
            return true;
        } else {
            return false;
        }
    }

    public User findById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public Iterable<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean createUser(String firstName, String lastName, String login, String password, String address) {
        User userFromDB = userRepository.findByUsername(login);
        if (userFromDB != null) {
            return false;
        }
        User user = new User(login, firstName, lastName, address);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    public boolean saveUser(User user) {
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public User getAuthorizedUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    public String getRoleName() {
        String roleName = "";
        for (Role role : getAuthorizedUser().getRoles().stream().toList()) {
            if (role.getName().equals("ROLE_USER")) {
                roleName = "user";
            } else {
                roleName = "admin";
            }
        }
        return roleName;
    }
}