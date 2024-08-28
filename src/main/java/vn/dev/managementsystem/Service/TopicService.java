package vn.dev.managementsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dev.managementsystem.Dto.TopicDto;
import vn.dev.managementsystem.Entity.Topic;
import vn.dev.managementsystem.Entity.User;
import vn.dev.managementsystem.Enum.TopicState;
import vn.dev.managementsystem.Repository.TopicRepository;
import vn.dev.managementsystem.Repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Topic> getAllTopic(){
        return topicRepository.findAll();
    }

    public List<Topic> orderByDate(){
        return null;
    }

    public List<Topic> orderByLecturer(){
        return topicRepository.getOrderByLecturer();
    }

    public List<Topic> orderByStatus(){
        return topicRepository.getOrderByStatus();
    }

    public Topic getById(Integer id){
        return topicRepository.findById(id).orElse(null);
    }

    public Topic saveAddTopic(TopicDto dto){
        Topic entity = dtoToTopic(dto);
        if (entity == null){
            return null;
        }
        entity.setCurrentState(String.valueOf(TopicState.PROPOSED));
        return topicRepository.save(entity);
    }

    public Topic dtoToTopic(TopicDto dto){
        Topic add = new Topic();
        User lecturer = userRepository.findById(dto.getLecturerId()).orElse(null);
        if (lecturer == null || !lecturer.getRole().equals("supervisor")){
            return null;
        }
        add.setSupervisor(lecturer);
        add.setCreate_date(LocalDate.now());
        add.setTitle(dto.getName());
        add.setLabel(dto.getLabel());
        add.setField(dto.getField());
        add.setPurpose(dto.getPurpose());
        add.setIdea(dto.getIdea());
        return add;
    }

    public List<Topic> orderByCreateDate() {
        return topicRepository.getOrderByCreateDate();
    }

    public Set<User> addStudentForTopic(Integer topicId, Integer userId) {
        Topic entity = topicRepository.findById(topicId).orElse(null);
        User uEn = userRepository.findById(userId).orElse(null);
        if (entity==null || uEn==null){
            return null;
        }
        entity.addRelationalStudent(uEn);
        topicRepository.save(entity);
        return entity.getStudentsOfTopic();
    }

    public Set<User> addStudentForTopic(Integer topicId, String email) {
        Topic entity = topicRepository.findById(topicId).orElse(null);
        User uEn = userRepository.getUserByName(email);

        System.out.println("Entity: " + ((Topic) entity).getTitle());
        System.out.println("uEn: " + uEn.getEmail());
        System.out.println("uEn role: " + uEn.getRole());
//        if (entity==null || uEn==null || !uEn.getRole().equals("researcher")
//                || uEn.getTopicOfStudent()!=null){
//            return null;
//        }
        
        entity.addRelationalStudent(uEn);
        topicRepository.save(entity);
        Set<User> r = entity.getStudentsOfTopic();
        System.out.println(r.size());
        return r;
    }

    public Topic setToApprove(Integer id, String email) {
        Topic entity = topicRepository.findById(id).orElse(null);
        User lecturerEn = userRepository.getUserByName(email);
        if (entity == null || !lecturerEn.getRole().equals("supervisor")){
            return null;
        }

        entity.setSupervisor(lecturerEn);
        entity.setPreviousState(entity.getCurrentState());
        entity.setCurrentState(String.valueOf(TopicState.APPROVED));
        return topicRepository.save(entity);
    }

    public Topic setToApprove(Integer id) {
        Topic entity = topicRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setPreviousState(entity.getCurrentState());
            entity.setCurrentState(String.valueOf(TopicState.IN_PROGRESS));
            return topicRepository.save(entity);
        }

        return null;
    }

    public Topic setToUnderReview(Integer id) {
        Topic entity = topicRepository.findById(id).orElse(null);
        if (entity == null){
            return null;
        }
        entity.setPreviousState(entity.getCurrentState());
        entity.setCurrentState(String.valueOf(TopicState.UNDER_REVIEW));
        return topicRepository.save(entity);
    }

    public Topic setToPauseOrResume(Integer id) {
        Topic entity = topicRepository.findById(id).orElse(null);
        if (entity == null){
            return null;
        }
        if(!entity.getCurrentState().equals("PAUSE")){
            entity.setPreviousState(entity.getCurrentState());
            entity.setCurrentState(String.valueOf(TopicState.PAUSE));
        }else {
            entity.setCurrentState(entity.getPreviousState());
        }
        return topicRepository.save(entity);
    }

    public Topic setToCancel(Integer id) {
        Topic entity = topicRepository.findById(id).orElse(null);
        if (entity == null){
            return null;
        }
        entity.setPreviousState(entity.getCurrentState());
        entity.setCurrentState(String.valueOf(TopicState.CANCEL));
        return topicRepository.save(entity);
    }

    public Topic setToScore(Integer id, String score) {
        Topic entity = topicRepository.findById(id).orElse(null);
        if (entity == null){
            return null;
        }
        entity.setScore(score);
        return topicRepository.save(entity);
    }
}
