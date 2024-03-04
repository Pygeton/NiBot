package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.repository.entity.AdminData;

import java.util.List;

public interface IAdminDataService extends IService<AdminData> {

    boolean isAdminExist(Long id);

    boolean isSuperAdmin(Long id);

    boolean addAdmin(Long id);

    boolean deleteAdmin(Long id);

    List<AdminData> getAdminList();
}
