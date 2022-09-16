package com.service;

import com.InfoUser;

import java.util.List;
import java.util.Map;


public interface InfoUserService {
    void ping();
    List<InfoUser> insertInfoUser(InfoUser infoUser);
    InfoUser getInfoUserById(String id);
    void deleteInfoUserById(String id);
    String getNameById(String id);
    Map<String,InfoUser> getAllUser();
}