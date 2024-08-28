package vn.dev.managementsystem.Dto;


import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.dev.managementsystem.Entity.User;
import vn.dev.managementsystem.Enum.TopicState;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicDto {

    private String name;

    private String label;

    private String field;

    private String purpose;

    private String idea;

    private Integer lecturerId;
}
