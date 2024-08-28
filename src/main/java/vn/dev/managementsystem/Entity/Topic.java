package vn.dev.managementsystem.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.dev.managementsystem.Enum.TopicState;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
//    @JsonIgnore
    private Integer id;

    @Column(name = "name", length = 500, nullable = false)
    private String title;

    @Column(name = "label", length = 500, nullable = true)
    private String label;

    @Column(name = "previous_state", length = 50, nullable = true)
    private String previousState;

    @Column(name = "current_state", length = 50, nullable = false)
    private String currentState = String.valueOf(TopicState.PROPOSED);

    @Column(name = "field", nullable = false)
    private String field;

    @Column(name = "purpose", nullable = true)
    private String purpose;

    @Column(name = "idea", nullable = true)
    private String idea;

    @Column(name = "create_date", nullable = true)
    private LocalDate create_date;

    @Column(name = "end_date", nullable = true)
    private LocalDate end_date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecturer_id")
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private User supervisor;

    @OneToOne(mappedBy = "topicOfGroup", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Group council;

    @Column(name = "score")
    private String score;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "topicOfStudent")
    private Set<User> studentsOfTopic = new HashSet<>();

    public void addRelationalStudent(User _topic) {
        studentsOfTopic.add(_topic);
        _topic.setTopicOfStudent(this);
    }

    public void deleteRelationalStudent(User _topic) {
        studentsOfTopic.remove(_topic);
        _topic.setTopicOfStudent(null);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "topicReport")
    private Set<Report> reports = new HashSet<>();

    public void addRelationalReport(Report _report) {
        reports.add(_report);
        _report.setTopicReport(this);
    }

    public void deleteRelationalReport(Report _report) {
        reports.remove(_report);
        _report.setTopicReport(null);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "topicAttachment")
    private Set<Document> documents = new HashSet<>();

    public void addRelationalAttachment(Document _attach) {
        documents.add(_attach);
        _attach.setTopicAttachment(this);
    }

    public void deleteRelationalAttachment(Document _attach) {
        documents.remove(_attach);
        _attach.setTopicAttachment(null);
    }
}

