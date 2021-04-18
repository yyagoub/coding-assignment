package com.coding.assignment.configuration.security;

import com.coding.assignment.configuration.exception.ErrorsHandler;
import com.coding.assignment.configuration.exception.UnprocessableEntityException;
import com.coding.assignment.configuration.exception.ValidationError;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ErrorsHandler errorsHandler;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUsersByUsername(username);
        if (user.isPresent())
            return user.get();
        throw new UsernameNotFoundException("user not found");
    }

    public UserDto authenticate(final AuthenticationDto authenticationDto) {
        // validate data, then authenticate user
        validateAuthenticationDto(authenticationDto);
        authenticateUser(authenticationDto);
        // return user data
        return getUserDtoWithToken(authenticationDto.getUsername());
    }

    private void validateAuthenticationDto(final AuthenticationDto authenticationDto){
        List<ValidationError> errors = new ArrayList<>();
        // validate
        if (authenticationDto.getUsername() == null || authenticationDto.getUsername().isEmpty())
            errorsHandler.publish("username", "field.required", errors);
        if (authenticationDto.getPassword() == null || authenticationDto.getPassword().isEmpty())
            errorsHandler.publish("password", "field.required", errors);
        // raise error if exists
        if (!CollectionUtils.isEmpty(errors))
            throw new UnprocessableEntityException(errors);
    }

    private void authenticateUser(final AuthenticationDto authenticationDto) {
        List<ValidationError> errors = new ArrayList<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.getUsername(), authenticationDto.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            errorsHandler.publish("username", "users.service.invalid.username.password", errors);
            errorsHandler.publish("password", "users.service.invalid.username.password", errors);
            throw new UnprocessableEntityException(errors);
        }
    }
    
    private UserDto getUserDtoWithToken(final String username){
        User user = loadUserByUsername(username);
        String token = generateNewToken(user);
        UserDto userDto = mapUserToUserDto(user);
        userDto.setToken(token);
        return userDto;
    }

    private String generateNewToken(final UserDetails userDetails) {
        return jwtTokenUtil.generateToken(userDetails);
    }

    private UserDto mapUserToUserDto(final User user){
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    public void saveUsers(List<User> users) {
        userRepository.saveAll(users);
    }
}
