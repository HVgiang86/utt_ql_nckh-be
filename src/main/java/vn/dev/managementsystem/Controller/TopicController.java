package vn.dev.managementsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dev.managementsystem.Dto.GetDBody;
import vn.dev.managementsystem.Dto.TopicAttachmentDto;
import vn.dev.managementsystem.Dto.TopicDto;
import vn.dev.managementsystem.Entity.*;
import vn.dev.managementsystem.Repository.GroupRepository;
import vn.dev.managementsystem.Repository.TopicRepository;
import vn.dev.managementsystem.Service.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {

    @Autowired
    IFirestoreService firestoreService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private GroupService groupService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllTopics() {
        List<Topic> topics = topicService.getAllTopic();
        if (topics == null) {
            return BaseResponse.<List<Topic>>builder().code(HttpStatus.NOT_FOUND.value()).message("Topics not found").data(null).build().toResponse();
        }
        return BaseResponse.<List<Topic>>builder().code(HttpStatus.OK.value()).message("Successfully").data(topics).build().toResponse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTopicById(@PathVariable Integer id) {
        try {
            Map<String, Object> jsonResult = new HashMap<>();
            Topic entity = topicService.getById(id);
            if (entity == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Topic>builder().code(HttpStatus.OK.value()).message("Successfully").data(entity).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/order-by-state")
    public ResponseEntity<Map<String, Object>> getTopicsByState() {
        try {
            Map<String, List<Topic>> jsonResult = new HashMap<>();
            List<Topic> ts = topicService.orderByStatus();
            return BaseResponse.<List<Topic>>builder().code(HttpStatus.OK.value()).message("Successfully").data(ts).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/order-by-supervisor")
    public ResponseEntity<Map<String, Object>> getTopicsByTeacher() {
        try {
            Map<String, List<Topic>> jsonResult = new HashMap<>();
            List<Topic> ts = topicService.orderByLecturer();
            return BaseResponse.<List<Topic>>builder().code(HttpStatus.OK.value()).message("Successfully").data(ts).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/order-by-createdate")
    public ResponseEntity<Map<String, Object>> getTopicsByDate() {
        try {
            Map<String, List<Topic>> jsonResult = new HashMap<>();
            List<Topic> ts = topicService.orderByCreateDate();
            return BaseResponse.<List<Topic>>builder().code(HttpStatus.OK.value()).message("Successfully").data(ts).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    //TODO-PRODUCE==========================================================
    @GetMapping(value = {"/{date}", "/{state}",})
    public List<Topic> getTopicsByKey(@PathVariable(value = "date", required = false) String date, @PathVariable(value = "state", required = false) String state) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<Map<String, Object>> createTopic(@RequestBody TopicDto topicDto) {
        try {
            Map<String, Object> jsonResult = new HashMap<>();
            Topic model = topicService.saveAddTopic(topicDto);
            if (model == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Lecturer not found").data(null).build().toResponse();
            }
            return BaseResponse.<Topic>builder().code(HttpStatus.OK.value()).message("Successfully").data(model).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/{email:.+}")
    public ResponseEntity<Map<String, Object>> getLecturerTopics(@PathVariable String email) {
        try {
            Map<String, Set<Topic>> jsonResult = new HashMap<>();
            if (userService.getUserByEmail(email) == null) {
                System.out.println("EMAIL Null");
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("User not found").data(null).build().toResponse();
            }
            Set<Topic> topicsOfLecturer = userService.getTopicsOfLecturer(email);
            System.out.println("EMAIL NOT Null: " + topicsOfLecturer.stream().findFirst());

            return BaseResponse.<Set<Topic>>builder().code(HttpStatus.OK.value()).message("Successfully").data(topicsOfLecturer).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }



    }

    @PostMapping(value = {"/{topicId}/{email}"})
    public ResponseEntity<Map<String, Object>> addStudentsForTopic(@PathVariable(value = "topicId") Integer topicId, @PathVariable(value = "email", required = false) String email) {

        try {
            Set<User> result = new HashSet<>();
            Map<String, Set<User>> jsonResult = new HashMap<>();
            System.out.println("id: " + topicId + " email: " + email);
            if (email != null) {
                result = topicService.addStudentForTopic(topicId, email);
                if (result == null) {
                    return BaseResponse.<Set<User>>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic or student not found").data(null).build().toResponse();
                }
            } else {
                return BaseResponse.<Set<User>>builder().code(HttpStatus.BAD_REQUEST.value()).message("Missing email param").data(null).build().toResponse();
            }
            jsonResult.put("Successfully", result);
            return BaseResponse.<Set<User>>builder().code(HttpStatus.OK.value()).message("Successfully").data(result).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/researchers/{topicId}")
    public ResponseEntity<Map<String, Object>> getStudentsOfTopic(@PathVariable Integer topicId) {
        try {
            Map<String, Set<User>> jsonResult = new HashMap<>();
            if (topicService.getById(topicId) == null) {
                return BaseResponse.<Set<User>>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            Set<User> result = topicService.getById(topicId).getStudentsOfTopic();
            return BaseResponse.<Set<User>>builder().code(HttpStatus.OK.value()).message("Successfully").data(result).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @PostMapping("/set-approve/{topicId}")
    public ResponseEntity<Map<String, Object>> setApproved(@PathVariable(value = "topicId", required = false) Integer topicId) {

        try {
            Map<String, Object> jsonResult = new HashMap<>();
            if (topicId == null) {
                return BaseResponse.<String>builder().code(HttpStatus.BAD_REQUEST.value()).message("Missing topic id").data(null).build().toResponse();
            }
            Topic t = topicService.setToApprove(topicId);
            if (t == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Topic>builder().code(HttpStatus.OK.value()).message("Successfully").data(t).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @PostMapping("/set-under-review/{topicId}")
    public ResponseEntity<Map<String, Object>> setUnderRev(@PathVariable(value = "topicId", required = false) Integer topicId) {
        try {
            Map<String, Object> jsonResult = new HashMap<>();
            if (topicId == null) {
                return BaseResponse.<String>builder().code(HttpStatus.BAD_REQUEST.value()).message("Missing topic id").data(null).build().toResponse();
            }
            Topic t = topicService.setToUnderReview(topicId);
            if (t == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Topic>builder().code(HttpStatus.OK.value()).message("Successfully").data(t).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @PostMapping("/set-pause-or-resume/{topicId}")
    public ResponseEntity<Map<String, Object>> setPauseOrResume(@PathVariable(value = "topicId", required = false) Integer topicId) {

        try {
            Map<String, Object> jsonResult = new HashMap<>();
            if (topicId == null) {
                return BaseResponse.<String>builder().code(HttpStatus.BAD_REQUEST.value()).message("Missing topic id").data(null).build().toResponse();
            }
            Topic t = topicService.setToPauseOrResume(topicId);
            if (t == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Topic>builder().code(HttpStatus.OK.value()).message("Successfully").data(t).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }



    }

    @PostMapping("/set-cancel/{topicId}")
    public ResponseEntity<Map<String, Object>> setCancel(@PathVariable(value = "topicId", required = false) Integer topicId) {

        try {
            Map<String, Object> jsonResult = new HashMap<>();
            if (topicId == null) {
                return BaseResponse.<String>builder().code(HttpStatus.BAD_REQUEST.value()).message("Missing topic id").data(null).build().toResponse();
            }
            Topic t = topicService.setToCancel(topicId);
            if (t == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Topic>builder().code(HttpStatus.OK.value()).message("Successfully").data(t).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

//    @PostMapping("/attachment/{topicId}")
//    public ResponseEntity<Map<String, Document>> addAttachForReport(
//            @PathVariable Integer topicId,
//            @RequestParam(value = "file", required = false) MultipartFile file,
//            @RequestParam(value = "name", required = false) String name){
//        Map<String, Document> jsonResult = new HashMap<>();
//        if(file == null){
//            jsonResult.put("Missing file param", null);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResult);
//        }
////        Document a = attachmentService.saveAddAttachmentOfTopic(topicId, name, file);
//        try {
//            String fileUrl = firestoreService.save(file);
//
//            Document a = attachmentService.saveUploadedAttachmentOfTopic(topicId, name, fileUrl);
//
//            if(a==null){
//                jsonResult.put("topic not found", null);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResult);
//            }
//
//            jsonResult.put("Successfully", a);
//            return ResponseEntity.status(HttpStatus.OK).body(jsonResult);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResult);
//        }
//    }

    //    @PostMapping("/attachment/{topicId}")
//    public ResponseEntity<Map<String, Document>> addAttachForReport(
//            @PathVariable Integer topicId, @RequestBody TopicAttachmentDto attachmentDto) {
//        Map<String, Document> jsonResult = new HashMap<>();
//
//        Document a = attachmentService.saveUploadedAttachmentOfTopic(topicId, attachmentDto);
//
//        if (a == null) {
//            jsonResult.put("topic not found", null);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResult);
//        }
//
//        jsonResult.put("Successfully", a);
//        return ResponseEntity.status(HttpStatus.OK).body(jsonResult);
//    }
    @PostMapping("/attachment/{topicId}")
    public ResponseEntity<Map<String, Object>> addAttachForReport(@PathVariable Integer topicId, @RequestBody TopicAttachmentDto attachmentDto) {
        try {
            Map<String, Document> jsonResult = new HashMap<>();

            Document a = attachmentService.saveUploadedAttachmentOfTopic(topicId, attachmentDto);

            if (a == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }

            return BaseResponse.<Document>builder().code(HttpStatus.OK.value()).message("Successfully").data(a).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/attachment/{topicId}")
    public ResponseEntity<Map<String, Object>> getAttachmentsOfReport(@PathVariable Integer topicId) {
        try {
            Map<String, Set<Document>> jsonResult = new HashMap<>();
            Set<Document> a = attachmentService.getListByTopicId(topicId);
            if (a == null) {
                return BaseResponse.<Set<Document>>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Set<Document>>builder().code(HttpStatus.OK.value()).message("Successfully").data(a).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }



    }

    @PostMapping("/set-score/{topicId}")
    public ResponseEntity<Map<String, Object>> setScore(@PathVariable(value = "topicId", required = false) Integer topicId, @RequestParam(value = "score", required = false) String score) {

        try {
            Map<String, Object> jsonResult = new HashMap<>();
            if (topicId == null) {
                return BaseResponse.<String>builder().code(HttpStatus.BAD_REQUEST.value()).message("Missing topic id").data(null).build().toResponse();
            }
            if (score == null) {
                return BaseResponse.<String>builder().code(HttpStatus.BAD_REQUEST.value()).message("Missing score param").data(null).build().toResponse();
            }
            Topic t = topicService.setToScore(topicId, score);
            if (t == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Topic>builder().code(HttpStatus.OK.value()).message("Successfully").data(t).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/council/{topicId}")
    public ResponseEntity<Map<String, Object>> getCouncil(@PathVariable Integer topicId) {

        try {
            Map<String, Object> jsonResult = new HashMap<>();
            Group g = groupService.getGroupByTopicId(topicId);
            if (g == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Group>builder().code(HttpStatus.OK.value()).message("Successfully").data(g).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @PostMapping("/council/{topicId}")
    public ResponseEntity<Map<String, Object>> addCouncil(@PathVariable Integer topicId, @RequestBody Group group) {

        try {
            Map<String, Object> jsonResult = new HashMap<>();
            Group g = groupService.saveAddGroup(topicId, group);
            if (g == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Group>builder().code(HttpStatus.OK.value()).message("Successfully").data(g).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @PutMapping("/council/{topicId}")
    public ResponseEntity<Map<String, Object>> updateCouncil(@PathVariable Integer topicId, @RequestBody Group group) {
        try {
            Map<String, Object> jsonResult = new HashMap<>();
            Group g = groupService.saveUpdateGroup(topicId, group);
            if (g == null) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Group not found").data(null).build().toResponse();
            }
            return BaseResponse.<Group>builder().code(HttpStatus.OK.value()).message("Success").data(g).build().toResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @PostMapping("/council/set-protect-time/{topicId}")
    public ResponseEntity<Map<String, Object>> setProtectTime(@PathVariable Integer topicId,
                                                              @RequestBody GetDBody date) throws ParseException {
        Map<String, Object> jsonResult = new HashMap<>();

        try {
            Group g = groupService.setProtectTime(topicId, date);
            //        Date g = groupService.setProtectTime(topicId, date);
            if (g == null){
                jsonResult.put("Topic not found", "704");
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Group>builder().code(HttpStatus.OK.value()).message("Successfully").data(g).build().toResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }

    }

}
