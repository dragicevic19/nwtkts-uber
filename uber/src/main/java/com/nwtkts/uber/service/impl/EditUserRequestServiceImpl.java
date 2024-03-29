package com.nwtkts.uber.service.impl;

import com.nwtkts.uber.dto.EditUserRequestDTO;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.EditUserRequest;
import com.nwtkts.uber.model.User;
import com.nwtkts.uber.repository.EditUserRequestRepository;
import com.nwtkts.uber.repository.UserRepository;
import com.nwtkts.uber.service.EditUserRequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditUserRequestServiceImpl implements EditUserRequestService{
    private final EditUserRequestRepository editUserRequestRepository;
    private final UserRepository userRepository;

    public EditUserRequestServiceImpl(EditUserRequestRepository editUserRequestRepository, UserRepository userRepository) {
        this.editUserRequestRepository = editUserRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<EditUserRequest> findByStatus(String status) {
        return editUserRequestRepository.findByStatus(status);
    }

    @Override
    public EditUserRequestDTO createNotification(EditUserRequestDTO editUserRequestDTO) {
        EditUserRequest request = new EditUserRequest(editUserRequestDTO);
        User user = userRepository.findById(editUserRequestDTO.getOldInfo().getId()).orElseThrow(() -> new NotFoundException("User does not exist"));
        request.setUser(user);
        EditUserRequest eurWithId = editUserRequestRepository.save(request);
        return new EditUserRequestDTO(eurWithId);
    }

    @Override
    public EditUserRequest changeStatus(EditUserRequestDTO request) {
        EditUserRequest found = editUserRequestRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Edit user request does not exist"));
        found.setStatus(request.getStatus());
        return editUserRequestRepository.save(found);
    }
}
