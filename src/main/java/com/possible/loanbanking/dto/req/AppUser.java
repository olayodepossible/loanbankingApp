package com.possible.loanbanking.dto.req;

import com.possible.loanbanking.model.Account;

import com.possible.loanbanking.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Invalid firstName: must not be empty")
    @NotNull(message = "Invalid firstName:  must not be NULL")
    @Size(min = 3, max = 30, message = "Invalid firstName: Must be of 3 - 30 characters")
    private String firstName;

    @NotBlank(message = "Invalid lastName: must not be empty")
    @NotNull(message = "Invalid lastName: must not be NULL")
    @Size(min = 3, max = 30, message = "Invalid lastName: Must be of 3 - 30 characters")
    private String lastName;

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Invalid Username: must not be empty")
    @NotNull(message = "Invalid Username:  must not be NULL")
    @Size(min = 3, max = 15, message = "Invalid UserName: Must be of 3 - 30 characters")
    private String username;

    @Min(value = 16, message = "Invalid Age: must be 16 years or more to register")
    @Max(value = 100, message = "Invalid Age: must not pass 100years")
    Integer age;

    @NotBlank(message = "Invalid Password: must not be empty")
    @NotNull(message = "Invalid Password:  must not be NULL")
    private String password;
    @NotNull(message = "User address must not be empty")

    @NotBlank(message = "Invalid Address: must not be empty")
    @NotNull(message = "Invalid Address:  must not be NULL")
    private String address;

    @NotBlank(message = "Invalid Phone number: must not be empty")
    @NotNull(message = "Invalid Phone number: must not be NULL")
    @Pattern(regexp = "^\\d{11}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Invalid Proof of Identity: must not be empty")
    @NotNull(message = "Invalid Proof of Identity:  must not be NULL")
    private String identityProof;

    private String accountNumber;
    private Boolean isEnable;

    @ManyToOne(cascade = CascadeType.ALL)
    private Role roles;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany( mappedBy="user", cascade = CascadeType.ALL)
    private List<Account> accountList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.roles.getRoleName()));
        return authorities;
    }

    public boolean setIsEnable(Boolean value){
        return value;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }


}
