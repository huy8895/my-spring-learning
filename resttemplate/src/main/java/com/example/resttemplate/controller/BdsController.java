package com.example.resttemplate.controller;

import com.example.resttemplate.CandidateBDSResDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController()
public class BdsController {

    //TODO anh Duy se implement api nay va tra ve danh sach CandidateBDSResDTO gom co thong tin cua BDS
    @PostMapping(value = "/v1/list-bds")
    public ResponseEntity<Object> getListBDSInfo(@RequestBody List<String> listKeycloak){
        List<CandidateBDSResDTO> result = new ArrayList<>();
        for (String s : listKeycloak) {
            CandidateBDSResDTO dto1 = new CandidateBDSResDTO(s, "mã code BDS_ " + s +" , Họ Tên, Quận/huyện, tỉnh/thành");
            result.add(dto1);
        }
        return ResponseEntity.ok(result);

    }
}
