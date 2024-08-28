package vn.dev.managementsystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "tbl_attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    @JsonIgnore
    private Integer id;

    @Column(name = "name", length = 500)
    private String name;

    @Column(name = "path", length = 500, nullable = false)
    private String path;

    @Column(name = "create_date")
    private Date create_date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id")
    private Topic topicAttachment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_id")
    private Report reportAttachment;
}
