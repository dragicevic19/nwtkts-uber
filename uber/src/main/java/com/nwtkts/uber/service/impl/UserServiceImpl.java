package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.ChangePasswordRequest;
import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.model.Address;
import com.nwtkts.uber.model.EditUserRequest;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.repository.EditUserRequestRepository;
import com.nwtkts.uber.repository.UserRepository;
import com.nwtkts.uber.service.EmailService;
import com.nwtkts.uber.service.PasswordResetTokenService;
import com.nwtkts.uber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EditUserRequestRepository editUserRequestRepository;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    @Autowired
    private EmailService emailService;


    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseGet(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    @Override
    public User register(User u, RegistrationRequest userRequest) {
        u.setEmail(userRequest.getEmail());
        if (userRequest.getPassword() != null)
            u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        u.setFirstName(userRequest.getFirstName());
        u.setLastName(userRequest.getLastName());
        u.setBlocked(false);
        u.setFullRegDone(false);
        u.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now().minusSeconds(1)));
        u.setPhoto("https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3.webp");
        u.setAddress(new Address());
        u.setPhoneNumber("");
        return u;
    }

    @Override
    public boolean checkIfUserExists(RegistrationRequest userRequest) {
        userRequest.setEmail(userRequest.getEmail().toLowerCase(Locale.ROOT));
        User existUser = this.userRepository.findByEmail(userRequest.getEmail());
        return existUser != null;
    }

    @Override
    public void resetPasswordRequest(User user) throws MessagingException, UnsupportedEncodingException {
        String token = passwordResetTokenService.generateToken(user);
        emailService.sendForgotPasswordMail(user, token);
    }

    @Override
    public void resetPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now().minusSeconds(1)));
        userRepository.save(user);
    }

//    @Override
//    public User editUserInfo(User loggedInUser, UserProfile editedUser) {
//        loggedInUser.setFirstName(editedUser.getFirstName());
//        loggedInUser.setLastName(editedUser.getLastName());
//        loggedInUser.setPhoneNumber(editedUser.getPhone());
//        loggedInUser.getAddress().setCity(editedUser.getCity());
//        loggedInUser.getAddress().setCountry(editedUser.getCountry());
//        loggedInUser.getAddress().setStreet(editedUser.getStreet());
//
//        return this.userRepository.save(loggedInUser);
//    }

    @Override
    public User editUserInfo(User loggedInUser, UserProfile editedUser) {
        EditUserRequest eur = new EditUserRequest(loggedInUser, editedUser);
        editUserRequestRepository.save(eur);
        return loggedInUser;
    }

    @Override
    public User changeProfilePicture(User loggedInUser, String picUrl) {
        loggedInUser.setPhoto(picUrl);
        return this.userRepository.save(loggedInUser);
    }

    @Override
    public User changePassword(User loggedInUser, ChangePasswordRequest request) {
        if (loggedInUser.getPassword() != null
                && !passwordEncoder.matches(request.getCurrentPassword(), loggedInUser.getPassword()))
            return null;

        if (!request.getPassword().equals(request.getRepPassword())) return null;

        loggedInUser.setPassword(passwordEncoder.encode(request.getPassword()));
        loggedInUser.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now().minusSeconds(1)));
        return this.userRepository.save(loggedInUser);
    }

//    public User updateUser(User user) {
//        return userRepository.save(user);
//    }
//
    public User updateUserFromUserProfile(UserProfile up){
        User user = findByEmail(up.getEmail());
        user.setEmail(up.getEmail());
        user.setFirstName(up.getFirstName());
        user.setLastName(up.getLastName());
        user.setPhoto(up.getImage());
        user.setPhoneNumber(up.getPhone());

        if (up.getStreet() != null && up.getCity() != null && up.getCountry() != null) {
            Address newAddress = new Address(up.getStreet(), up.getCity(), up.getCountry());
            user.setAddress(newAddress);
        }
        return userRepository.save(user);
    }

}
