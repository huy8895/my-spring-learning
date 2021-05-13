package com.example.resttemplate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CandidateBDSResDTO implements Serializable{
    public String keycloakId;

    /**
     * + Format: mã code BDS, Họ Tên, Quận/huyện, tỉnh/thành
     */
    public String bdsInfo;
}
