package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.ResetPasswordDto;
import com.nwtkts.uber.model.PasswordResetToken;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.repository.PasswordResetTokenRepository;
import com.nwtkts.uber.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken prToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(prToken);
        return token;
    }

    @Override
    public PasswordResetToken validatePasswordResetToken(ResetPasswordDto payload) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(payload.getToken());
        if (token == null) return null;

        if (!token.getUser().getEmail().equalsIgnoreCase(payload.getEmail())) return null;

        return token;
    }
}
