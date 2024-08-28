package vn.dev.managementsystem.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class UserRequestDto {

    private String fullName;

    private String email;

    private String password;

    private LocalDate dateOfBirth;

    private String description;

    private String phoneNumber;

    private String faculty;

    private String department;

    private String title;

    private String major;

    private String className;


    public UserRequestDto(String fullName, String email, String password, LocalDate dateOfBirth,
                          String description, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

    public UserRequestDto() {
    }
}
