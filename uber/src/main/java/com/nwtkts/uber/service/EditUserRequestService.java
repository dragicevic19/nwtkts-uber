package com.nwtkts.uber.service;

import com.nwtkts.uber.dto.EditUserRequestDTO;
import com.nwtkts.uber.model.EditUserRequest;

import java.util.List;

public interface EditUserRequestService {
    List<EditUserRequest> findAll();
    EditUserRequestDTO createNotification(EditUserRequestDTO editUserRequest);
    EditUserRequest changeStatus(EditUserRequestDTO request);
}
