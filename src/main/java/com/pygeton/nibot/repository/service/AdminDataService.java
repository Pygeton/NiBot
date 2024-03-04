package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.AdminData;
import com.pygeton.nibot.repository.mapper.AdminDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDataService extends ServiceImpl<AdminDataMapper, AdminData> implements IAdminDataService {

    @Override
    public boolean isAdminExist(Long id) {
        AdminData data = getById(id);
        return data != null;
    }

    @Override
    public boolean isSuperAdmin(Long id) {
        AdminData data = getById(id);
        return data.getIsSuper();
    }

    @Override
    public boolean addAdmin(Long id) {
        return save(new AdminData(id,false));
    }

    @Override
    public boolean deleteAdmin(Long id) {
        return removeById(id);
    }

    @Override
    public List<AdminData> getAdminList() {
        return list();
    }
}
