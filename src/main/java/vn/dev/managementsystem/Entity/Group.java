package vn.dev.managementsystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_group")
public class Group {
    @Id
    @Column(name = "topic_id")
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE)
    @MapsId
    @JoinColumn(name = "topic_id")
    @JsonIgnore
    private Topic topicOfGroup;

    @Column(name = "lecturer1_email")
    private String lecturer1Email;

    @Column(name = "lecturer2_email")
    private String lecturer2Email;

    @Column(name = "lecturer3_email")
    private String lecturer3Email;

    @Column(name = "lecturer4_email")
    private String lecturer4Email;

    @Column(name = "lecturer5_email")
    private String lecturer5Email;

    @Column(name = "protect_time", nullable = true)
    private Date protectTime;

}
