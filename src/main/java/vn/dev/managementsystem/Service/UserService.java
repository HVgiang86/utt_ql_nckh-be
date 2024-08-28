package vn.dev.managementsystem.Service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.dev.managementsystem.Dto.UserRequestDto;
import vn.dev.managementsystem.Entity.Topic;
import vn.dev.managementsystem.Entity.User;
import vn.dev.managementsystem.Repository.RoleRepository;
import vn.dev.managementsystem.Repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByName(email);
    }

    public boolean saveAddAdmin (UserRequestDto requestUser){
        User add = dtoToUser(requestUser);
        if(add!=null){
            add.addRole(roleRepository.getRoleByName("admin"));
            add.setStatus(true);
            add.setRole("admin");
            userRepository.save(add);
            return true;
        }
        return false;
    }

    public boolean saveAddLecturer (UserRequestDto requestUser){
        User add = dtoToUser(requestUser);
        if(add!=null) {
            add.addRole(roleRepository.getRoleByName("supervisor"));
            add.setStatus(true);
            add.setRole("supervisor");
            userRepository.save(add);
            return true;
        }
        return false;
    }

    public boolean saveAddStudent (UserRequestDto requestUser){
        User add = dtoToUser(requestUser);
        if(add!=null) {
            add.addRole(roleRepository.getRoleByName("researcher"));
            add.setStatus(true);
            add.setRole("researcher");
            userRepository.save(add);
            return true;
        }
        return false;
    }

    public boolean checkExistEmail(String email){
        List<User> all = userRepository.findAll();
        for(User user: all){
            if (user.getEmail().equals(email) && user.getStatus()){
                return true;
            }
        }
        return false;
    }

    public boolean checkPassword(String email, String password){
        User entity = getUserByEmail(email);
        BCryptPasswordEncoder b = new BCryptPasswordEncoder(4);
        return b.matches(password, entity.getPassword());
    }

    User dtoToUser(UserRequestDto dto){
        if(dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            dto.setPassword(new BCryptPasswordEncoder(4).encode(dto.getPassword()));
        }else {
//            throw new NullPointerException("Mật khẩu không để trống");
            return null;
        }

        User add = new User();
        add.setFullName(dto.getFullName());
        add.setEmail(dto.getEmail());
        add.setPassword(dto.getPassword());
        add.setDateOfBirth(dto.getDateOfBirth());
        add.setDescription(dto.getDescription());
        add.setPhoneNumber(dto.getPhoneNumber());
        return add;
    }

    public List<User> getUserListByRoleName(String name){
        return roleRepository.getRoleByName(name).getUsers();
    }

    public boolean changeStatus(String email){
        User model = getUserByEmail(email);
        if(model != null){
            model.setStatus(!model.getStatus());
            userRepository.save(model);
            return true;
        }else {
            return false;
        }
    }

    public boolean delete(String email){
        User model = getUserByEmail(email);
        if(model != null){
            userRepository.delete(model);
            return true;
        }else {
            return false;
        }
    }

    public boolean setPassword(String email, String password){
        User model = getUserByEmail(email);
        if(model != null){
            model.setPassword(new BCryptPasswordEncoder(4).encode(password));
            return true;
        }else {
            return false;
        }
    }

    public User uploadAvatar(String url, String email) {
        User entity = getUserByEmail(email);
        entity.setAvatar(url);
        userRepository.save(entity);
        return entity;
    }

    public User update(String email, UserRequestDto dto) {
        User model = getUserByEmail(email);
        if (model == null) {
            return null;
        }

        model.setFullName(dto.getFullName() != null ? dto.getFullName() : model.getFullName());
        model.setDateOfBirth(dto.getDateOfBirth() != null ? dto.getDateOfBirth() : model.getDateOfBirth());
        model.setFaculty(dto.getFaculty() != null ? dto.getFaculty() : model.getFaculty());
        model.setDepartment(dto.getDepartment() != null ? dto.getDepartment() : model.getDepartment());
        model.setTitle(dto.getTitle() != null ? dto.getTitle() : model.getTitle());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        model.setMajor(dto.getMajor() != null ? dto.getMajor() : model.getMajor());
        model.setClassName(dto.getClassName() != null ? dto.getClassName() : model.getClassName());
        userRepository.save(model);
        return model;
    }

    public ByteArrayInputStream generateCsv(List<User> users) throws IOException {
        try (ByteArrayInputStream stream = usersToCsv(users)) {
            System.out.println("LIST SIZE: SS" + stream);
            return stream;
        }
    }

    private ByteArrayInputStream usersToCsv(List<User> users) throws IOException {
        String[] formartHeader = {"id","full_name","email","date_of_birth","description","phone_number","role"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(formartHeader);
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format))
        {
            for(User user: users){
                String[] data = {String.valueOf(user.getId()), user.getFullName() ,user.getEmail(),
                        user.getDateOfBirth() != null ?  user.getDateOfBirth().toString() : "",
                        user.getDescription(),
                        user.getPhoneNumber(), user.getRole()
                };
                printer.printRecords((Object) data);
                printer.flush();
            }
            printer.flush();
            return new ByteArrayInputStream(out.toByteArray());
        }catch (IOException e){
        }
        return null;
    }


    public Set<Topic> getTopicsOfLecturer(String email) {
        User u = getUserByEmail(email);
        return u.getTopicsOfLecturer();
    }
}
