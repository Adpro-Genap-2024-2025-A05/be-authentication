package id.ac.ui.cs.advprog.beauthentication.model;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Nested
    class LifecycleTests {
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
    }

    @Nested
    class UserDetailsImplementationTests {
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
        void getAuthoritiesReturnsEmptyListWhenRoleIsNull() {
            User user = User.builder().build();

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            assertTrue(authorities.isEmpty());
        }

        @Test
        void accountStatusMethodsReturnTrue() {
            User user = new User();

            assertTrue(user.isAccountNonExpired());
            assertTrue(user.isAccountNonLocked());
            assertTrue(user.isCredentialsNonExpired());
            assertTrue(user.isEnabled());
        }
    }

    @Nested
    class BuilderTests {
        @Test
        void builderCreatesUserWithAllProperties() {
            User user = createFullUser();

            assertEquals("user-id", user.getId());
            assertEquals("user@example.com", user.getEmail());
            assertEquals("password", user.getPassword());
            assertEquals("User Name", user.getName());
            assertEquals("1234567890123456", user.getNik());
            assertEquals("User Address", user.getAddress());
            assertEquals("1234567890", user.getPhoneNumber());
            assertEquals(Role.PACILIAN, user.getRole());
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            User user = new User();
            setAllUserProperties(user);

            assertEquals("user-id", user.getId());
            assertEquals("user@example.com", user.getEmail());
            assertEquals("password", user.getPassword());
            assertEquals("User Name", user.getName());
            assertEquals("1234567890123456", user.getNik());
            assertEquals("User Address", user.getAddress());
            assertEquals("1234567890", user.getPhoneNumber());
            assertEquals(Role.PACILIAN, user.getRole());
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForSameId() {
            User user1 = User.builder().id("user-id").build();
            User user2 = User.builder().id("user-id").build();

            assertEquals(user1, user2);
        }

        @Test
        void equalsReturnsFalseForDifferentId() {
            User user1 = User.builder().id("user-id").build();
            User user2 = User.builder().id("different-id").build();

            assertNotEquals(user1, user2);
        }

        @Test
        void hashCodeReturnsSameValueForEqualObjects() {
            User user1 = User.builder().id("user-id").email("user@example.com").build();
            User user2 = User.builder().id("user-id").email("user@example.com").build();

            assertEquals(user1.hashCode(), user2.hashCode());
        }
    }

    private User createFullUser() {
        return User.builder()
                .id("user-id")
                .email("user@example.com")
                .password("password")
                .name("User Name")
                .nik("1234567890123456")
                .address("User Address")
                .phoneNumber("1234567890")
                .role(Role.PACILIAN)
                .build();
    }

    private void setAllUserProperties(User user) {
        user.setId("user-id");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setName("User Name");
        user.setNik("1234567890123456");
        user.setAddress("User Address");
        user.setPhoneNumber("1234567890");
        user.setRole(Role.PACILIAN);
    }
}