package vn.dev.managementsystem.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import vn.dev.managementsystem.Entity.Report;
import vn.dev.managementsystem.Entity.Topic;

import java.util.Date;

public class AttachmentDto {

    private String name;

    private Integer topicId;

    private Integer reportId;
}
