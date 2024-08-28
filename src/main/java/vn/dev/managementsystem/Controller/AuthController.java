package vn.dev.managementsystem.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dev.managementsystem.Dto.AuthenticationRequest;
import vn.dev.managementsystem.Dto.AuthenticationResponse;
import vn.dev.managementsystem.Dto.UserRequestDto;
import vn.dev.managementsystem.Entity.BaseResponse;
import vn.dev.managementsystem.Entity.User;
import vn.dev.managementsystem.Service.AuthenticationService;
import vn.dev.managementsystem.Service.UserService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
                        @RequestBody AuthenticationRequest authenticationRequest){
        try {
            Map<String, Object> jsonResult = new HashMap<>();
            boolean check = userService.checkExistEmail(authenticationRequest.getUsername());
            if (!check){
                return BaseResponse.<String>builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("Email not found")
                        .data(null)
                        .build().toResponse();
            }
            boolean checkP = userService.checkPassword(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword());
            if (!checkP){
                return BaseResponse.<String>builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message("Not exactly password")
                        .data(null)
                        .build().toResponse();
            }
            session.setAttribute("username", authenticationRequest.getUsername());
            AuthenticationResponse model = authenticationService.authenticate(authenticationRequest);
            return BaseResponse.<AuthenticationResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Login successfully")
                    .data(model)
                    .build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody UserRequestDto requestUser){
        try {
            Map<String, Object> jsonResult = new HashMap<>();
            boolean check = userService.checkExistEmail(requestUser.getEmail());
            System.out.println("asddadas");

            if (check){
                System.out.println("asddadas");
                return BaseResponse.<String>builder()
                        .code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Email already exist")
                        .data(null)
                        .build().toResponse();
            }
            boolean c = userService.saveAddStudent(requestUser);
            if (!c){
                return BaseResponse.<String>builder()
                        .code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("Invalid password")
                        .data(null)
                        .build().toResponse();
            }
            User model = userService.getUserByEmail(requestUser.getEmail());
            return BaseResponse.<User>builder()
                    .code(HttpStatus.OK.value())
                    .message("Register successfully")
                    .data(model)
                    .build().toResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .data(null)
                    .build().toResponse();
        }
    }
}
