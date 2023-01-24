package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.EditUserRequestDTO;
import com.nwtkts.uber.model.EditUserRequest;

import java.util.List;

public interface EditUserRequestService {
    List<EditUserRequest> findByStatus(String status);
    EditUserRequestDTO createNotification(EditUserRequestDTO editUserRequest);
    EditUserRequest changeStatus(EditUserRequestDTO request);
}
