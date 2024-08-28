package vn.dev.managementsystem.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.dev.managementsystem.Entity.Topic;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

    private String content;

    private String link;

    private Integer topicId;
}
