package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.ResetPasswordDto;
import com.nwtkts.uber.model.PasswordResetToken;
import com.nwtkts.uber.model.User;

public interface PasswordResetTokenService {
    String generateToken(User user);
    PasswordResetToken validatePasswordResetToken(ResetPasswordDto payload);
}
