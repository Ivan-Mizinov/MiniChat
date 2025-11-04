package io.synergy.minichat.service;

import io.synergy.minichat.entity.RoleEntity;
import io.synergy.minichat.entity.UserEntity;
import io.synergy.minichat.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_admin_success() {
        RoleEntity adminRole = new RoleEntity("ROLE_ADMIN");
        Set<RoleEntity> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setRoles(adminRoles);
        when(userRepository.findByUsername("admin")).thenReturn(admin);

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertTrue(((UserEntity) userDetails).getRoles().contains(adminRole));
    }

    @Test
    void loadUserByUsername_user_success() {
        RoleEntity userRole = new RoleEntity("ROLE_USER");
        Set<RoleEntity> userRoles = new HashSet<>();
        userRoles.add(userRole);

        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setRoles(userRoles);

        when(userRepository.findByUsername("user")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("user");

        assertNotNull(userDetails);
        assertEquals("user", userDetails.getUsername());
        assertTrue(((UserEntity) userDetails).getRoles().contains(userRole));
    }

    @Test
    void loadUserByUsername_invalidUsername_throwsException() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("nonexistent")
        );
    }
}