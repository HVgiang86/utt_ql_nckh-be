package vn.dev.managementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResearchResponse {
    private Integer studentId;

    private String fullName;

    private String email;

    private String password;

    private LocalDate dateOfBirth;

    private String major;

    private String className;
}
