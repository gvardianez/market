package ru.alov.market.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alov.market.api.dto.ChangePasswordDto;
import ru.alov.market.api.dto.RegisterUserDto;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.auth.repositories.UserRepository;
import ru.alov.market.auth.entities.Role;
import ru.alov.market.auth.entities.User;
import ru.alov.market.auth.validators.ChangePasswordValidator;
import ru.alov.market.auth.validators.RegistrationValidator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RegistrationValidator registrationValidator;
    private final ChangePasswordValidator changePasswordValidator;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional
    public User createNewUser(RegisterUserDto registerUserDto) {
        registrationValidator.validate(registerUserDto);
        if (findByUsername(registerUserDto.getUsername()).isPresent())
            throw new IllegalStateException("Имя пользователя уже используется");
        if (findUserByEmail(registerUserDto.getEmail()).isPresent())
            throw new IllegalStateException("Email уже используется");
        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setEmail(registerUserDto.getEmail());
        user.setRoles(List.of(roleService.getUserRole()));
        user.setEmailStatus(User.UserStatus.MAIL_NOT_CONFIRMED.toString());
        return userRepository.save(user);
    }

    public void confirmUserEmail(String username, String email) {
        User user = getUser(username);
        if (!user.getEmail().equals(email)) {
            throw new IllegalStateException("Email пользователя не совпадает с указанным при регистрации");
        }
        user.setEmailStatus(User.UserStatus.MAIL_CONFIRMED.toString());
        userRepository.save(user);
    }

    public void changePassword(String username, ChangePasswordDto changePasswordDto) {
        changePasswordValidator.validate(changePasswordDto);
        User user = getUser(username);
        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword()))
            throw new BadCredentialsException("Неверно указан старый пароль");
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    public String recoverPassword(String username) {
        User user = getUser(username);
        if (user.getEmailStatus().equals(User.UserStatus.MAIL_NOT_CONFIRMED.toString()))
            throw new IllegalStateException("Для восстановления пароля необходимо подтвредить Email");
        String newPassword = String.valueOf((int) (Math.random() * (10000000)));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return newPassword;
    }

    private User getUser(String username) {
        return findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(String.format("Пользователь '%s' не найден", username)));
    }

}