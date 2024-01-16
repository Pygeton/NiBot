package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pygeton.nibot.communication.entity.mai.MaimaiRating;
import com.pygeton.nibot.repository.entity.MaimaiRatingData;

import java.util.List;

public interface IMaimaiRatingDataService extends IService<MaimaiRatingData> {

    List<MaimaiRating> getRatingList(int min);
}
