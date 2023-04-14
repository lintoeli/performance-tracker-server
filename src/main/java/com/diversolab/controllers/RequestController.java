package com.diversolab.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diversolab.entities.Request;
import com.diversolab.servicies.RequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @CrossOrigin
    @PostMapping("/new")
    public String newRequest(@RequestBody String strBody){

        System.out.println(strBody);
        JSONObject body = new JSONObject(strBody);
        String address = body.getString("address");
        JSONObject res = new JSONObject();

        Request request = requestService.newRequest(address);

        if(request != null){
            res.put("created", true);
        } else {
            res.put("created", false);
        }

        return res.toString();
    }

}