package com.possible.loanbanking.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role implements Serializable {
    private static final long serialVersionUID = 2405172041950251807L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginDto {

        private String username;
        private String password;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDto {
       private String firstName;
       private String lastName;
       private String email;
       private String username;
       private Integer age;
       private String password;
       private String address;
       private String phoneNumber;
       private String identityProof;
    }
}
