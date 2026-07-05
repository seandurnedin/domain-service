package com.libraryapp.domain.repository;

import com.libraryapp.domain.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(path = "user-groups", collectionResourceRel = "userGroups")
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @RestResource(path = "by-name", rel = "byName")
    Optional<UserGroup> findByName(@Param("name") String name);
}
