package com.ohgiraffers.hitechautoworks.res.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ohgiraffers.hitechautoworks.auth.dto.UserDTO;
import com.ohgiraffers.hitechautoworks.auth.dto.resdto.EtcCarDTO;
import com.ohgiraffers.hitechautoworks.auth.dto.resdto.ImportantDTO;
import com.ohgiraffers.hitechautoworks.auth.dto.resdto.NormalDTO;
import com.ohgiraffers.hitechautoworks.auth.dto.resdto.SomoDTO;
import com.ohgiraffers.hitechautoworks.res.dto.ResDTO;
import com.ohgiraffers.hitechautoworks.auth.service.Details.AuthUserInfo;
import com.ohgiraffers.hitechautoworks.auth.service.UserService;
import com.ohgiraffers.hitechautoworks.res.service.ResService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResController {


    @Autowired
    private UserService userService;

    @Autowired
    private ResService resService;

    private AuthUserInfo authUserInfo;

    @GetMapping("/customer/res/res")
    public String res(Model model){
        List<ResDTO> resList = resService.findAllres();
        System.out.println("resList = " + resList);
        model.addAttribute("resList", resList);
        authUserInfo = new AuthUserInfo();
        UserDTO userDTO = authUserInfo.getUserDTO();
        String userName = userDTO.getUserName();
        model.addAttribute("userName",userName);
        return "customer/res/res";
    }
    @GetMapping("/customer/res/resDetail")
    public void resdetail(@RequestParam int resCode, Model model){
        System.out.println("resCode = "+resCode);
        ResDTO res = resService.findUserRes(resCode);
        System.out.println("res = " + res);
        model.addAttribute("res", res);
        authUserInfo = new AuthUserInfo();
        UserDTO userDTO = authUserInfo.getUserDTO();
        String userName = userDTO.getUserName();
        model.addAttribute("userName",userName);
    }
    @PostMapping("/customer/res/res")
    public void res1(@RequestParam int resCode, Model model){
        System.out.println("resCode = " + resCode);
        List<ResDTO> resList = resService.findCodeRes(resCode);
        System.out.println("resList = " + resList);
        model.addAttribute("resList", resList);

    }

    @GetMapping("/customer/res/res_01")
    public void res01() {
    }

    @GetMapping("/customer/res/res_02")
    public void res02() {
    }

    @GetMapping("/customer/res/res_03")
    public void res03() {
    }

    @GetMapping("/customer/res/res_04")
    public void res04() {
    }

    @GetMapping("/customer/res/res_05")
    public void res05() {
    }

    @GetMapping("/customer/res/res_06")
    public void res06(@ModelAttribute SomoDTO somoDTO, Model model,
                      @ModelAttribute ImportantDTO importantDTO,
                      @ModelAttribute NormalDTO normalDTO,
                      @ModelAttribute EtcCarDTO etcCarDTO,
                      HttpSession httpSession) {
        List<String> partAllList = (List<String>) httpSession.getAttribute("partAllList");

        List<String> partList = somoDTO.getNonNullValues();
        List<String> partList2 = importantDTO.getNonNullValues();
        List<String> partList3 = normalDTO.getNonNullValues();
        List<String> partList4 = etcCarDTO.getNonNullValues();

        if (partAllList == null) {
            partAllList = new ArrayList<>();
        }

        partAllList.addAll(partList);
        partAllList.addAll(partList2);
        partAllList.addAll(partList3);
        partAllList.addAll(partList4);

        httpSession.setAttribute("partAllList", partAllList);

        model.addAttribute("partlist", partAllList);
    }

    @GetMapping("/customer/res/res_07")
    public void res07(Model model) {
        authUserInfo = new AuthUserInfo();
        UserDTO userDTO = authUserInfo.getUserDTO();
        String userName = userDTO.getUserName();
        model.addAttribute("userName",userName);
    }

    @PostMapping("/customer/res/res_07")
    public void res071(@RequestBody String date, HttpSession httpSession){
        System.out.println("date = " + date);
        //"date":"2024-05-13"
        JsonObject jsonObject = JsonParser.parseString(date).getAsJsonObject();
        String dateValue = jsonObject.get("date").getAsString();
        System.out.println("dateValue = " + dateValue);
        Date date1 = Date.valueOf(dateValue);

    }

    @GetMapping("/customer/res/res_08")
    public void res08() {
    }
}