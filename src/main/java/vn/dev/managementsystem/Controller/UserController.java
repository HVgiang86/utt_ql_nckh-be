package vn.dev.managementsystem.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.dev.managementsystem.Dto.ResearchResponse;
import vn.dev.managementsystem.Dto.SupervisorResponse;
import vn.dev.managementsystem.Dto.UserRequestDto;
import vn.dev.managementsystem.Entity.BaseResponse;
import vn.dev.managementsystem.Entity.Topic;
import vn.dev.managementsystem.Entity.User;
import vn.dev.managementsystem.Service.CloudinaryService;
import vn.dev.managementsystem.Service.IFirestoreService;
import vn.dev.managementsystem.Service.UserService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    IFirestoreService firestoreService;
    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private HttpSession session;

    @PostMapping(value = "/create-admin")
    public ResponseEntity<Map<String, Object>> createAdmin(
            @RequestBody UserRequestDto requestUser) {
        try {
            Map<String, Object> jsonResult = new HashMap<>();
            boolean check = userService.checkExistEmail(requestUser.getEmail());
            if (check) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Email already exist")
                        .data(null)
                        .build().toResponse();
            }
            boolean c = userService.saveAddAdmin(requestUser);
            if (!c) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Invalid password")
                        .data(null)
                        .build().toResponse();
            }
            User model = userService.getUserByEmail(requestUser.getEmail());
            return BaseResponse.<User>builder().code(HttpStatus.OK.value())
                    .message("Create admin success")
                    .data(model)
                    .build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

    @PostMapping(value = "/create-supervisor")
    public ResponseEntity<Map<String, Object>> createLecturer(
            @RequestBody UserRequestDto requestUser) {
        try {
            Map<String, Object> jsonResult = new HashMap<>();
            boolean check = userService.checkExistEmail(requestUser.getEmail());
            if (check) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Email already exist")
                        .data(null)
                        .build().toResponse();
            }
            boolean c = userService.saveAddLecturer(requestUser);
            if (!c) {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Invalid password")
                        .data(null)
                        .build().toResponse();
            }
            User model = userService.getUserByEmail(requestUser.getEmail());
            return BaseResponse.<User>builder().code(HttpStatus.OK.value())
                    .message("Create supervisor success")
                    .data(model)
                    .build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

    @GetMapping(value = {"", "/{role}"})
    public ResponseEntity<Map<String, Object>> getUserList(@PathVariable(value = "role", required = false) String role) {
        try {
            Map<String, List<User>> jsonResult = new HashMap<>();
            if (role == null) {
                List<User> models = userService.getAllUsers();
                return BaseResponse.<List<User>>builder().code(HttpStatus.OK.value())
                        .message("Get User List successfully")
                        .data(models)
                        .build().toResponse();
            } else if (role.equals("admin") || role.equals("supervisor") || role.equals("researcher")) {
                List<User> models = userService.getUserListByRoleName(role);
                return BaseResponse.<List<User>>builder().code(HttpStatus.OK.value())
                        .message("Get User List successfully")
                        .data(models)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("Role name not exactly")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

    @PreAuthorize("hasAnyAuthority('supervisor', 'admin', 'researcher')")
    @GetMapping("/researcher-profile")
    public ResponseEntity<Map<String, Object>> researcherProfile() {
        try {
            String email = (String) session.getAttribute("username");
            User model = userService.getUserByEmail(email);

            ResearchResponse responseModel = new ResearchResponse();
            responseModel.setFullName(model.getFullName());
            responseModel.setEmail(model.getEmail());
            responseModel.setPassword(model.getPassword());
            responseModel.setStudentId(model.getId());
            responseModel.setMajor(model.getMajor());
            responseModel.setDateOfBirth(model.getDateOfBirth());
            responseModel.setClassName(model.getClassName());

            Map<String, Object> jsonResult = new HashMap<>();
            if (model.getId() > 0) {
                return BaseResponse.<ResearchResponse>builder().code(HttpStatus.OK.value())
                        .message("Get User successfully")
                        .data(responseModel)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

    @PreAuthorize("hasAnyAuthority('supervisor', 'admin', 'researcher')")
    @GetMapping("/supervisor-profile")
    public ResponseEntity<Map<String, Object>> supervisorProfile() {
        try {
            String email = (String) session.getAttribute("username");
            User model = userService.getUserByEmail(email);
            SupervisorResponse responseModel = new SupervisorResponse();
            responseModel.setFullName(model.getFullName());
            responseModel.setEmail(model.getEmail());
            responseModel.setPassword(model.getPassword());
            responseModel.setDepartment(model.getDepartment());
            responseModel.setFaculty(model.getFaculty());
            responseModel.setTitle(model.getTitle());

            Map<String, Object> jsonResult = new HashMap<>();
            if (model.getId() > 0) {
                return BaseResponse.<SupervisorResponse>builder().code(HttpStatus.OK.value())
                        .message("Get User successfully")
                        .data(responseModel)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

    @PreAuthorize("hasAnyAuthority('supervisor', 'admin', 'researcher')")
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> profile() {
        try {
            String email = (String) session.getAttribute("username");
            System.out.println("Email: " + email);
            User model = userService.getUserByEmail(email);
            Map<String, Object> jsonResult = new HashMap<>();
            if (model != null) {
                return BaseResponse.<User>builder().code(HttpStatus.OK.value())
                        .message("Get User successfully")
                        .data(model)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }


    @GetMapping("/get/{email:.+}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String email) {
        try {
            User model = userService.getUserByEmail(email);
            Map<String, Object> jsonResult = new HashMap<>();
            if (model != null) {
                return BaseResponse.<User>builder().code(HttpStatus.OK.value())
                        .message("Get User successfully")
                        .data(model)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

    @PostMapping(value = {"/change-status/{email:.+}", "/delete/{email:.+}"})
    public ResponseEntity<Map<String, Object>> inactiveUser(@PathVariable String email) {
        try {
            boolean execute = userService.changeStatus(email);
            if (execute) {
                return BaseResponse.<String>builder().code(HttpStatus.OK.value())
                        .message("successfully")
                        .data(null)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

//    @DeleteMapping("/delete/{email:.+}")
//    public ResponseEntity<String> delete(@PathVariable String email){
//        boolean execute = userService.delete(email);
//        if(execute){
//            return ResponseEntity.ok("successfully");
//        }else {
//            return ResponseEntity.ok("User not found");
//        }
//    }

    @PostMapping("/upload-avatar/{email:.+}")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable String email) {
        try {
            Map data = this.cloudinaryService.upload(file);
            User model = userService.uploadAvatar(data.get("url").toString(), email);
            Map<String, Object> jsonResult = new HashMap<>();
            if (model != null) {
                return BaseResponse.<User>builder().code(HttpStatus.OK.value())
                        .message("successfully")
                        .data(model)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

    @PreAuthorize("hasAnyAuthority('supervisor', 'admin', 'researcher')")
    @PostMapping("/set-password/{email:.+}")
    public ResponseEntity<Map<String, Object>> setPassword(@PathVariable String email, @RequestParam String password) {
        try {
            if (password == null || password.trim().isEmpty()) {
                return BaseResponse.<String>builder().code(HttpStatus.BAD_REQUEST.value())
                        .message("Password is required")
                        .data(null)
                        .build().toResponse();
            }
            boolean execute = userService.setPassword(email, password.trim());
            if (execute) {
                return BaseResponse.<String>builder().code(HttpStatus.OK.value())
                        .message("successfully")
                        .data(null)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }

    @PutMapping("/put/{email:.+}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable String email, @RequestBody UserRequestDto dto) {
        try {
            User model = userService.update(email, dto);
            Map<String, Object> jsonResult = new HashMap<>();
            if (model != null) {
                return BaseResponse.<User>builder().code(HttpStatus.OK.value())
                        .message("successfully")
                        .data(model)
                        .build().toResponse();
            } else {
                return BaseResponse.<String>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .data(null)
                        .build().toResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }


    @GetMapping("/export/{role}")
    public ResponseEntity<Resource> exportCsv(@PathVariable String role) throws IOException {
        String fileName = "export" + role + ".csv";
        if (!role.equals("admin") && !role.equals("supervisor") && !role.equals("researcherresearcher")) {
            return ResponseEntity.ofNullable(null);
        }
        List<User> users = userService.getUserListByRoleName(role);
        System.out.println("LIST SIZE: " + users.size());
        ByteArrayInputStream fileData = userService.generateCsv(users);
        InputStreamResource resource = new InputStreamResource(fileData);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv")).body(resource);

    }

    @PreAuthorize("hasAnyAuthority('supervisor', 'admin')")
    @GetMapping("/topics/{email:.+}")
    public ResponseEntity<Map<String, Object>> getLecturerTopics(@PathVariable String email) {
        try {
            Map<String, Set<Topic>> jsonResult = new HashMap<>();
            if (userService.getUserByEmail(email) == null) {
                jsonResult.put("email not found", new HashSet<>());
                return BaseResponse.<Set<Topic>>builder().code(HttpStatus.NOT_FOUND.value())
                        .message("Email not found")
                        .data(new HashSet<>())
                        .build().toResponse();
            }
            Set<Topic> topicsOfLecturer = userService.getTopicsOfLecturer(email);
            jsonResult.put("successfully", topicsOfLecturer);
            return BaseResponse.<Set<Topic>>builder().code(HttpStatus.OK.value())
                    .message("Get topics successfully")
                    .data(topicsOfLecturer)
                    .build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }

    }
}
