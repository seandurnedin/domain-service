package com.libraryapp.domain.repository;

import com.libraryapp.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "notifications", collectionResourceRel = "notifications")
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @RestResource(path = "by-user", rel = "byUser")
    List<Notification> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @RestResource(path = "unread-by-user", rel = "unreadByUser")
    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(@Param("userId") Long userId);
}
