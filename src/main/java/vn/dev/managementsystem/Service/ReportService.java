package vn.dev.managementsystem.Service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dev.managementsystem.Dto.ReportDto;
import vn.dev.managementsystem.Entity.Report;
import vn.dev.managementsystem.Entity.Topic;
import vn.dev.managementsystem.Entity.User;
import vn.dev.managementsystem.Repository.ReportRepository;
import vn.dev.managementsystem.Repository.TopicRepository;
import vn.dev.managementsystem.Repository.UserRepository;

import java.util.Date;
import java.util.Set;

@Service
public class ReportService {

    @Autowired
    private HttpSession session;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    public Report getById(Integer id){
        return reportRepository.findById(id).orElse(null);
    }

    public Set<Report> getReportsOfTopic(Integer topicId){
        Topic t = topicRepository.findById(topicId).orElse(null);
        if(t == null){
            return null;
        }
        return t.getReports();
    }

    public Report saveAddReport(ReportDto reportDto, User user){
        Topic t = topicRepository.findById(reportDto.getTopicId()).orElse(null);
        if(t == null){
            return null;
        }
        Report r = new Report();
        r.setContent(reportDto.getContent());
        r.setLink(reportDto.getLink());
        r.setTopicReport(t);
        r.setCreate_date(new Date());
        r.setReporter(user.getId().toString());

        return reportRepository.save(r);
    }
}
