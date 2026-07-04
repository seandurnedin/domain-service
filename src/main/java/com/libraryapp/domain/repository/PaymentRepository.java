package com.libraryapp.domain.repository;

import com.libraryapp.domain.entity.Payment;
import com.libraryapp.domain.enums.EPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "payments", collectionResourceRel = "payments")
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @RestResource(path = "by-user", rel = "byUser")
    List<Payment> findByUserId(@Param("userId") Long userId);

    @RestResource(path = "by-user-and-status", rel = "byUserAndStatus")
    List<Payment> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") EPaymentStatus status);
}
