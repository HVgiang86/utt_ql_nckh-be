package vn.dev.managementsystem.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.dev.managementsystem.Dto.ReportDto;
import vn.dev.managementsystem.Entity.BaseResponse;
import vn.dev.managementsystem.Entity.Document;
import vn.dev.managementsystem.Entity.Report;
import vn.dev.managementsystem.Entity.User;
import vn.dev.managementsystem.Service.AttachmentService;
import vn.dev.managementsystem.Service.IFirestoreService;
import vn.dev.managementsystem.Service.ReportService;
import vn.dev.managementsystem.Service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    IFirestoreService firestoreService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportService reportService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private HttpSession session;

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<Map<String, Object>> getReportByTopicId(@PathVariable Integer topicId) {
        try {
            Map<String, Set<Report>> jsonResult = new HashMap<>();
            Set<Report> rs = reportService.getReportsOfTopic(topicId);
            if (rs == null) {
                return BaseResponse.<Set<Report>>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Set<Report>>builder().code(HttpStatus.OK.value()).message("Successfully").data(rs).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<Map<String, Object>> getReportById(@PathVariable Integer reportId) {
        try {
            Map<String, Report> jsonResult = new HashMap<>();
            Report rs = reportService.getById(reportId);
            if (rs == null) {
                return BaseResponse.<Report>builder().code(HttpStatus.NOT_FOUND.value()).message("Report not found").data(null).build().toResponse();
            }
            return BaseResponse.<Report>builder().code(HttpStatus.OK.value()).message("Successfully").data(rs).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @PostMapping("/topic/{topicId}")
    public ResponseEntity<Map<String, Object>> addReportByTopicId(@PathVariable Integer topicId, @RequestBody ReportDto dto) {
        try {
            Map<String, Report> jsonResult = new HashMap<>();
            dto.setTopicId(topicId);
            String email = (String) session.getAttribute("username");

            System.out.println("User Email: " + email);

            User model = userService.getUserByEmail(email);

            if (model == null) {
                return BaseResponse.<Report>builder().code(HttpStatus.NOT_FOUND.value()).message("User not found").data(null).build().toResponse();
            }

            Report rs = reportService.saveAddReport(dto, model);
            if (rs == null) {
                return BaseResponse.<Report>builder().code(HttpStatus.NOT_FOUND.value()).message("Topic not found").data(null).build().toResponse();
            }
            return BaseResponse.<Report>builder().code(HttpStatus.OK.value()).message("Successfully").data(rs).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @PostMapping("/attachment/{reportId}")
    public ResponseEntity<Map<String, Object>> addAttachForReport(@PathVariable Integer reportId, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "name", required = false) String name) {
        Map<String, Document> jsonResult = new HashMap<>();
        if (file == null) {
            return BaseResponse.<Document>builder().code(HttpStatus.BAD_REQUEST.value()).message("Missing file param").data(null).build().toResponse();
        }

        try {
            String fileUrl = firestoreService.save(file);

            Document a = attachmentService.saveAddAttachmentOfReport(reportId, name, fileUrl);

            if (a == null) {
                return BaseResponse.<Document>builder().code(HttpStatus.NOT_FOUND.value()).message("Report not found").data(null).build().toResponse();
            }

            return BaseResponse.<Document>builder().code(HttpStatus.OK.value()).message("Successfully").data(a).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<Document>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }

    @GetMapping("/attachment/{reportId}")
    public ResponseEntity<Map<String, Object>> getAttachmentsForReport(@PathVariable Integer reportId) {

        try {
            Map<String, Set<Document>> jsonResult = new HashMap<>();
            Set<Document> a = attachmentService.getListByReportId(reportId);
            if (a == null) {
                return BaseResponse.<Set<Document>>builder().code(HttpStatus.NOT_FOUND.value()).message("Report not found").data(null).build().toResponse();
            }
            System.out.println("=============AIS" + a.size());
            return BaseResponse.<Set<Document>>builder().code(HttpStatus.OK.value()).message("Successfully").data(a).build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error").data(null).build().toResponse();
        }
    }
}
