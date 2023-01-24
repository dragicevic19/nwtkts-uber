package com.nwtkts.uber.repository;

import com.nwtkts.uber.model.EditUserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EditUserRequestRepository  extends JpaRepository<EditUserRequest, Long> {
    List<EditUserRequest> findByStatus(String status);
}
