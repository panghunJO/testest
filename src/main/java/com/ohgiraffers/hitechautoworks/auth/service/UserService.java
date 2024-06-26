// UserService.java

package com.ohgiraffers.hitechautoworks.auth.service;

import com.ohgiraffers.hitechautoworks.auth.dao.UserMapper;
import com.ohgiraffers.hitechautoworks.auth.dto.*;
import com.ohgiraffers.hitechautoworks.part.dto.PartDTO;
import com.ohgiraffers.hitechautoworks.repair.dto.RepairDTO;
import com.ohgiraffers.hitechautoworks.res.dto.ResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class UserService  {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${file.upload-dir}")
    private String uploadDir;




    public String overlappedID(String id) {
        String result = userMapper.overlappedID(id);
        return result;
    }

    @Async
    public int regist(UserRegistDTO registDTO) {
        registDTO.setPass(passwordEncoder.encode(registDTO.getPass()));
        int result = 0;
        try {
            result = userMapper.regist(registDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public UserDTO findByUsername(String username) {

        UserDTO login = userMapper.findByUsername(username);

        if(!Objects.isNull(login)){
            return login;
        } else {
            return null;
        }
    }

    public int findcheck(int checknumber, String userId) {
        userMapper.findcheck(checknumber,userId);

        return 1;
    }



    public UserRegistDTO getAll(int userCode) {
        return userMapper.getAll(userCode);
    }

    public void deletePeople(int userCode) {
        userMapper.deletePeople(userCode);
    }

    public UserDTO findUserCode(int userCode) {
        return userMapper.findUserCode(userCode);
    }

    public int changepass(String currentPassword, String newPassword, String pw, int userCode) {
        if(passwordEncoder.matches(currentPassword,pw)) {
            if(newPassword.equals(currentPassword)){
                return 2;
            } else {
                String encodepw = passwordEncoder.encode(newPassword);
                userMapper.changepass(encodepw, userCode);
                return 1;
            }
        } else {
            return 0;
        }

    }

    public void updateUser(Map<String, Object> myprofile) {
        userMapper.updateUser(myprofile);
    }


    public ChartResponseDTO getpartchart() {

        List<Map<String, Object>> chart = userMapper.getPartChart();
        List<Integer> partStock = new ArrayList<>();
        List<String> partName = new ArrayList<>();

        for (Map<String, Object> entry : chart) {
            partStock.add((Integer) entry.get("part_stock"));
            partName.add((String) entry.get("part_name"));
        }

        return new ChartResponseDTO(partStock, partName);
    }

    public Map<String, Integer> getpersonchart() {
        List<Map<String,Integer>> listPerson = userMapper.getPersonChart();
        Map<String,Integer> map = new HashMap<>();

        for(Map<String,Integer> result : listPerson ){
            String role = String.valueOf(result.get("user_role"));
            Integer count = ((Number) result.get("role_count")).intValue();
            map.put(role, count);
        }

        return map;
    }

    public List<String>getTime(Date date1) {

        int AllemployeeCount = userMapper.getCustomerCount();
        List<Map<String,Object>> count = userMapper.getTimeCount(date1);

        int checkNull = 0;
        String thisTime = "";
        List<String> disabledTimesList = new ArrayList<>();
        for (Map<String, Object> a : count) {


            Object rawTime = a.get("time");
            Object rawTimeCount = a.get("employeeCount");
            Object extraTime = a.get("extraTime");

            int time = Integer.parseInt(rawTime.toString());
            int employeeCount = ((Number) rawTimeCount).intValue();
            int extraTimeCount = ((Number) extraTime).intValue();

            log.info("extraTimeCount {} ", extraTimeCount);

            for(int i = 0; i < extraTimeCount; i++) {

                switch (time) {
                    case 9:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "09:00:00"; time++;
                        break;
                    case 10:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "10:00:00"; time++;
                        break;
                    case 11:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "11:00:00"; time++;
                        break;
                    case 12:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "12:00:00"; time++;
                        break;
                    case 13:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "13:00:00"; time++;
                        break;
                    case 14:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "14:00:00"; time++;
                        break;
                    case 15:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "15:00:00"; time++;
                        break;
                    case 16:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "16:00:00"; time++;
                        break;
                    case 17:
                        checkNull = AllemployeeCount - employeeCount;
                        thisTime = "17:00:00"; time++;
                        break;
                    default:
                        log.info("Default case");
                        break;
                }

                if (checkNull <= 0) {
                    disabledTimesList.add(thisTime);
                }
            }
        }
        return disabledTimesList;
    }

    public List<Map<String, Object>> getCalendar(int userCode) {
        List<Map<String, Object>> getCalendar = userMapper.getCalendar(userCode);
        log.info("getCalendar {} ", getCalendar);
        List<Map<String, Object>> updatedCalendar = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        for (Map<String, Object> event : getCalendar) {
            String title = (String) event.get("title");
            LocalDateTime start = (LocalDateTime) event.get("start");
            int extraTime = (int) event.get("extraTime");

            LocalDateTime end = start.plusHours(extraTime);

            Map<String, Object> updatedEvent = new HashMap<>(event);
            updatedEvent.put("start", start.format(formatter));
            updatedEvent.put("end", end.format(formatter));
            updatedEvent.put("title", title);
            updatedCalendar.add(updatedEvent);
        }

        return updatedCalendar;
    }

    public Map<String, Object> searchForId(Object email) {
        String idForEmail = String.valueOf(email);
        Map<String,Object> gogoEmail = userMapper.searchForId(idForEmail);

        return gogoEmail;
    }

    public Map<String, Object> searchForPW(Map<String, Object> info) {
        String PWForPhone = (String) info.get("phone");
        String PWForId = (String) info.get("Id");

        Map<String,Object> goPass = new HashMap<>();
        int result = userMapper.findPW(PWForId,PWForPhone);
        if(result != 0){
            int newPw = (int) ((Math.random() * 900000) + 100000);
            String newPw1 = String.valueOf(newPw);
            String encodePw = passwordEncoder.encode(newPw1);
            userMapper.changePassForId(encodePw,PWForId);
            goPass.put("newPass",newPw);
        }

        return goPass;
    }

    public int submitReply(Map<String, Object> info) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        info.put("registTime",formattedNow);

        return userMapper.submitReply(info);
    }

    public Map<String,Object> getReplyComment(int resReplyCode) {
        return userMapper.searchReply(resReplyCode);
    }

    public List<ContactDTO> contactList() { return userMapper.contactList();
    }

    public ContactDTO selectContact(int code) {
        return userMapper.selectContact(code);
    }

    public void deleteContact(int contactCode) {
        userMapper.deleteContact(contactCode);
    }

    public List<ContactDTO> selectContactByCode(int contactCode) {
        return userMapper.selectContactByCode(contactCode);
    }

    public List<ContactDTO> selectContactByName(String userName) {
        return userMapper.selectContactByName(userName);
    }

    public void changeContact(int contactCode) {
        userMapper.changeContact(contactCode);
    }

    public void changeStatus(int contactCode) {
        userMapper.changeStatus(contactCode);
    }

    public String findContactStatus(int contactCode) {
        return userMapper.findContactStatus(contactCode);
    }

    public int submitContact(Map<String, Object> info) {
        return userMapper.submitContact(info);
    }

    public void saveNote(Map<String, Object> info) {
        userMapper.saveNote(info);
    }

    public Map<String, Object> getNote() {
        return userMapper.getNote();
    }


    public List<RepairDTO> repairnoti(int userCode) {  return userMapper.repairnoti(userCode);
    }

    public List<PartDTO> partnoti() {
        return userMapper.partnoti();
    }

    public List<Map<String, Object>> getContactCommit() {
        return userMapper.getContactCommit();
    }

//    public int imgUpload(MultipartFile img, int userCode){
//
//        Resource resource = resourceLoader.getResource("classpath:static/img/profile");
//        String filePath = null;
//
//        if (!resource.exists()) {
//            // 만약 static 폴더에 파일이 없는 경우 만들어준다.
//            String root = "src/main/resources/static/img/profile";
//            File file = new File(root);
//
//            file.mkdirs();  // 폴더 만들기
//
//            filePath = file.getAbsolutePath();
//        } else {
//            try {
//                filePath = resourceLoader.getResource("classpath:static/img/profile").getFile().getAbsolutePath();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        String originalFilename = img.getOriginalFilename();
//
//        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
//
//        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
//
//        try {
//            img.transferTo(new File(filePath + "/" + savedName));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        String filepath = "/img/profile/" + savedName;
//
//        int result = userMapper.uploadimg(filepath,userCode);
//
//        return result;
//    }

    public int imgUpload(MultipartFile img, int userCode) {

        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();  // 폴더 만들기
        }

        String originalFilename = img.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

        try {
            img.transferTo(new File(directory + "/" + savedName));
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }

        String filepath = "/images/profile/" + savedName;

        int result = userMapper.uploadimg(filepath, userCode);

        return result;
    }

    public List<ContactDTO> myContact(int userCode) {
        return userMapper.myContact(userCode);
    }

    public void contactStatus(String contactCode) {
      userMapper.cotactStatus(contactCode);
    }

    public List<ContactDTO> contactnoti() {
        return userMapper.contactnoti();
    }

    public AuthenticDTO emailCheck(String info) {
        return userMapper.emailCheck(info);
    }

    public List<Map<String, Object>> getAdminCalendar() {
        return userMapper.getAdminCalendar();
    }
}
