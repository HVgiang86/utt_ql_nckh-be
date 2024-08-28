package vn.dev.managementsystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tbl_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
//    @JsonIgnore
    private Integer id;

    @Column(name = "full_name", length = 60, nullable = false)
    private String fullName;

    @Column(name = "email", length = 225, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    // for lecturer/supervisor
    @Column(name = "faculty", length = 225)
    private String faculty;

    // for lecturer/supervisor
    @Column(name = "department", length = 225)
    private String department;

    // for lecturer/supervisor
    @Column(name = "title", length = 225)
    private String title;

    @Column(name = "avatar", length = 255, nullable = true)
    private String avatar;

    // for student
    @Column(name = "major", length = 255, nullable = true)
    private String major;

    // for student
    @Column(name = "class_name", length = 255, nullable = true)
    private String className;

    // for student
    @Column(name = "date_of_birth", length = 45, nullable = true)
    private LocalDate dateOfBirth;

    @Column(name = "description", length = 1000, nullable = true)
    private String description;

    @Column(name = "phone_number", length = 20, nullable = true)
    private String phoneNumber;

    @Column(name = "role", length = 45, nullable = true)
    private String role;

    @Column(name = "status", nullable = true)
    private Boolean status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id")
    private Topic topicOfStudent;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "users")
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        role.getUsers().add(this);
        roles.add(role);
    }

    public void deleteRole(Role role) {
        role.getUsers().remove(this);
        roles.remove(role);
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "supervisor")
    private Set<Topic> topicsOfLecturer = new HashSet<>();

    public void addRelationalTopic(Topic _topic) {
        topicsOfLecturer.add(_topic);
        _topic.setSupervisor(this);
    }

    public void deleteRelationalTopic(Topic _topic) {
        topicsOfLecturer.remove(_topic);
        _topic.setSupervisor(null);
    }


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
