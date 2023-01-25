package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.ChangePasswordRequest;
import com.nwtkts.uber.dto.RegistrationRequest;
import com.nwtkts.uber.dto.UserProfile;
import com.nwtkts.uber.model.Ride;
import com.nwtkts.uber.model.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    User findById(Long userId) throws AccessDeniedException;
    List<User> findAll() throws AccessDeniedException;
    User findByEmail(String name);
    User register(User d, RegistrationRequest userRequest);
    boolean checkIfUserExists(RegistrationRequest userRequest);
    void resetPasswordRequest(User user) throws MessagingException, UnsupportedEncodingException;
    void resetPassword(User user, String password);
    User editUserInfo(User loggedInUser, UserProfile editedUser);

    User changeProfilePicture(User loggedInUser, String picUrl);

    User changePassword(User loggedInUser, ChangePasswordRequest request);
    User updateUserFromUserProfile(UserProfile up);

    void blockUser(Long id);

    void unblockUser(Long id);
}
