package vn.dev.managementsystem.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.dev.managementsystem.Dto.TopicAttachmentDto;
import vn.dev.managementsystem.Entity.Document;
import vn.dev.managementsystem.Entity.Report;
import vn.dev.managementsystem.Entity.Topic;
import vn.dev.managementsystem.Repository.AttachmentRepository;
import vn.dev.managementsystem.Repository.ReportRepository;
import vn.dev.managementsystem.Repository.TopicRepository;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private  CloudinaryService cloudinaryService;

    public Document saveAddAttachmentOfReport(Integer reportId, String name, String link) {
        Report r = reportRepository.findById(reportId).orElse(null);
        if(r == null){
            return null;
        }

        Document a = new Document();
        a.setTitle(name);
        a.setLink(link);
        a.setCreate_date(new Date());
        a.setReportAttachment(r);
        return attachmentRepository.save(a);
    }

    public Set<Document> getListByReportId(Integer reportId){
        Report r = reportRepository.findById(reportId).orElse(null);
        if(r == null){
            return null;
        }
        return r.getDocuments();
    }


    public Document saveAddAttachmentOfTopic(Integer topicId, String name, MultipartFile file){
        Topic r = topicRepository.findById(topicId).orElse(null);
        if(r == null){
            return null;
        }
        Map data = this.cloudinaryService.upload(file);
        Document a = new Document();
        a.setTitle(name);
        a.setLink(data.get("url").toString());
        a.setCreate_date(new Date());
        a.setTopicAttachment(r);
        return attachmentRepository.save(a);
    }

    public Document saveUploadedAttachmentOfTopic(Integer topicId, String name, String link){
        Topic r = topicRepository.findById(topicId).orElse(null);
        if(r == null){
            return null;
        }
        Document a = new Document();
        a.setTitle(name);
        a.setLink(link);
        a.setCreate_date(new Date());
        a.setTopicAttachment(r);
        return attachmentRepository.save(a);
    }

    public Document saveUploadedAttachmentOfTopic(Integer topicId, TopicAttachmentDto dto){
        Topic r = topicRepository.findById(topicId).orElse(null);
        if(r == null){
            return null;
        }
        Document a = new Document();
        a.setTitle(dto.getName());
        a.setLink(dto.getUrl());
        a.setCreate_date(new Date());
        a.setTopicAttachment(r);
        return attachmentRepository.save(a);
    }

    public Set<Document> getListByTopicId(Integer topicId){
        Topic r = topicRepository.findById(topicId).orElse(null);
        if(r == null){
            return null;
        }
        return r.getDocuments();
    }

}
