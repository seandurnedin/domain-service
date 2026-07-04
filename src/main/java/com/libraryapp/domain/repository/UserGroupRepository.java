package com.libraryapp.domain.repository;

import com.libraryapp.domain.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user-groups", collectionResourceRel = "userGroups")
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
