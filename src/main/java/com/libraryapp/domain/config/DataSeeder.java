package com.libraryapp.domain.config;

import com.libraryapp.domain.entity.User;
import com.libraryapp.domain.entity.UserGroup;
import com.libraryapp.domain.enums.ERole;
import com.libraryapp.domain.enums.EUserStatus;
import com.libraryapp.domain.repository.BookRepository;
import com.libraryapp.domain.repository.UserGroupRepository;
import com.libraryapp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

/**
 * Populates a fresh database with a handful of sample books (via {@code db/seed/books.sql}),
 * an ADMIN and a MANAGER user group, and an admin/manager account in each group - so there's
 * something to log in with and browse immediately after first startup. Gated by
 * {@link DataSeedProperties#isEnabled()} (app.data-seed.enabled) - off by default so it never
 * runs unexpectedly against a database that already has real data.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final String BOOKS_SEED_SCRIPT = "db/seed/books.sql";

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final DataSeedProperties properties;
    private final DataSource dataSource;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        if (!properties.isEnabled()) {
            return;
        }
        if (userRepository.count() > 0 || bookRepository.count() > 0 || userGroupRepository.count() > 0) {
            log.info("Startup data seed skipped: users/groups/books already exist");
            return;
        }

        seedBooks();
        seedUserGroupsAndUsers();
        log.info("Startup data seed complete: {} sample books, admin user '{}', manager user '{}'",
                bookRepository.count(), properties.getAdminUsername(), properties.getManagerUsername());
    }

    private void seedBooks() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(BOOKS_SEED_SCRIPT));
        populator.execute(dataSource);
    }

    private void seedUserGroupsAndUsers() {
        UserGroup adminGroup = userGroupRepository.save(UserGroup.builder()
                .name("ADMIN")
                .description("Library administrators - default borrowing limits, needed since admins also borrow under their own account")
                .build());

        UserGroup managerGroup = userGroupRepository.save(UserGroup.builder()
                .name("MANAGER")
                .description("Store managers - default borrowing limits, needed since managers also borrow under their own account")
                .build());

        userGroupRepository.save(UserGroup.builder()
                .name("USER")
                .description("Standard members - default borrowing limits")
                .build());

        User admin = User.builder()
                .username(properties.getAdminUsername())
                .email(properties.getAdminEmail())
                .passwordHash(passwordEncoder.encode(properties.getAdminPassword()))
                .fullName("Admin")
                .status(EUserStatus.ACTIVE)
                .role(ERole.ADMIN)
                .userGroup(adminGroup)
                .build();

        User manager = User.builder()
                .username(properties.getManagerUsername())
                .email(properties.getManagerEmail())
                .passwordHash(passwordEncoder.encode(properties.getManagerPassword()))
                .fullName("Manager")
                .status(EUserStatus.ACTIVE)
                .role(ERole.MANAGER)
                .userGroup(managerGroup)
                .build();

        userRepository.saveAll(List.of(admin, manager));
    }
}
