package id.ac.ui.cs.advprog.beauthentication.model;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void onCreateGeneratesUUID() {
        User user = new User();
        assertNull(user.getId());
        user.onCreate();
        assertNotNull(user.getId());
        assertEquals(36, user.getId().length());
    }

    @Test
    void onCreateDoesNotChangeExistingId() {
        User user = new User();
        String initialId = "existing-id-value";
        user.setId(initialId);

        user.onCreate();

        assertEquals(initialId, user.getId());
    }

    @Test
    void getUsernameReturnsEmail() {
        User user = User.builder()
                .email("user@example.com")
                .build();

        assertEquals("user@example.com", user.getUsername());
    }

    @Test
    void getAuthoritiesReturnsRoleWithPrefix() {
        User user = User.builder()
                .role(Role.PACILIAN)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_PACILIAN")));
    }

    @Test
    void accountStatusMethodsReturnTrue() {
        User user = User.builder().build();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void builderCreatesUser() {
        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .password("password")
                .name("User Name")
                .nik("1234567890123456")
                .address("User Address")
                .phoneNumber("1234567890")
                .role(Role.PACILIAN)
                .build();

        assertEquals("user-id", user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("User Name", user.getName());
        assertEquals("1234567890123456", user.getNik());
        assertEquals("User Address", user.getAddress());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals(Role.PACILIAN, user.getRole());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        User user = new User();

        user.setId("user-id");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setName("User Name");
        user.setNik("1234567890123456");
        user.setAddress("User Address");
        user.setPhoneNumber("1234567890");
        user.setRole(Role.PACILIAN);

        assertEquals("user-id", user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("User Name", user.getName());
        assertEquals("1234567890123456", user.getNik());
        assertEquals("User Address", user.getAddress());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals(Role.PACILIAN, user.getRole());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        User user1 = User.builder()
                .id("user-id")
                .email("user@example.com")
                .build();

        User user2 = User.builder()
                .id("user-id")
                .email("user@example.com")
                .build();

        User user3 = User.builder()
                .id("different-id")
                .email("different@example.com")
                .build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}