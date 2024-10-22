package org.marakobz.repository;

import org.marakobz.model.AdminRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {
    List<AdminRequest> findByStatus(String status);
}
