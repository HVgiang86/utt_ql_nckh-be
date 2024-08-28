package vn.dev.managementsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dev.managementsystem.Dto.GetDBody;
import vn.dev.managementsystem.Entity.Group;
import vn.dev.managementsystem.Entity.Topic;
import vn.dev.managementsystem.Repository.GroupRepository;
import vn.dev.managementsystem.Repository.TopicRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TopicRepository topicRepository;

    public Group getGroupByTopicId(Integer id){
        Topic t = topicRepository.findById(id).orElse(null);
        if(t==null){
            return null;
        }
        return t.getCouncil();
    }

    public Group saveAddGroup(Integer id,Group g){
        Topic t = topicRepository.findById(id).orElse(null);
        if(t==null){
            return null;
        }
        if(t.getCouncil() != null){
            return saveUpdateGroup(id,g);
        }
        g.setId(id);
        g.setTopicOfGroup(t);
        g.setProtectTime(new Date());
        return groupRepository.save(g);
    }

    public Group saveUpdateGroup(Integer id,Group g){
        Group entity = groupRepository.findById(id).orElse(null);
        if(entity == null){
            return null;
        }
        entity.setLecturer1Email(g.getLecturer1Email() != null ? g.getLecturer1Email() : entity.getLecturer1Email());
        entity.setLecturer2Email(g.getLecturer2Email() != null ? g.getLecturer2Email() : entity.getLecturer2Email());
        entity.setLecturer3Email(g.getLecturer3Email() != null ? g.getLecturer3Email() : entity.getLecturer3Email());
        entity.setLecturer4Email(g.getLecturer4Email() != null ? g.getLecturer4Email() : entity.getLecturer4Email());
        entity.setLecturer5Email(g.getLecturer5Email() != null ? g.getLecturer5Email() : entity.getLecturer5Email());
        return groupRepository.save(entity);
    }

    public Group setProtectTime(Integer topicId, GetDBody _date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parseDate = sdf.parse(_date.getDate());
        //        String date = sdf.format(parseDate);
        //        System.out.println("DATE EEEEEEEE == " + date);
        Group entity = Objects.requireNonNull(topicRepository.findById(topicId).orElse(null)).getCouncil();
        entity.setProtectTime(parseDate);
        groupRepository.save(entity);
        return entity;
    }
}
