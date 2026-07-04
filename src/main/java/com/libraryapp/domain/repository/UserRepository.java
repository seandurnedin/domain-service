package com.libraryapp.domain.repository;

import com.libraryapp.domain.entity.User;
import com.libraryapp.domain.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "users", collectionResourceRel = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    @RestResource(path = "by-username", rel = "byUsername")
    Optional<User> findByUsername(@Param("username") String username);

    @RestResource(path = "by-email", rel = "byEmail")
    Optional<User> findByEmail(@Param("email") String email);

    @RestResource(path = "exists-by-username", rel = "existsByUsername")
    boolean existsByUsername(@Param("username") String username);

    @RestResource(path = "exists-by-email", rel = "existsByEmail")
    boolean existsByEmail(@Param("email") String email);

    @RestResource(path = "by-role", rel = "byRole")
    List<User> findByRole(@Param("role") ERole role);
}
