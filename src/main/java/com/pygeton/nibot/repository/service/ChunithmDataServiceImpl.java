package com.pygeton.nibot.repository.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.pojo.ChunithmData;
import com.pygeton.nibot.repository.mapper.ChunithmDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ChunithmDataServiceImpl extends ServiceImpl<ChunithmDataMapper, ChunithmData> implements IChunithmDataService{

    @Override
    public boolean updateFromSega(List<JSONObject> list) {
        try {
            for (JSONObject object : list){
                ChunithmData data = new ChunithmData();
                data.setOfficialId(Integer.valueOf(object.getString("id")));
                data.setGenre(object.getString("catname"));
                data.setTitle(object.getString("title"));
                data.setTitleKana(object.getString("reading"));
                data.setArtist(object.getString("artist"));
                data.setIsEnabled(false);
                if(!object.getString("we_kanji").equals("")){
                    data.setWeType(object.getString("we_kanji"));
                    data.setWeStar(Integer.valueOf(object.getString("we_star")));
                }
                else {
                    data.setBasicLevel(object.getString("lev_bas"));
                    data.setAdvancedLevel(object.getString("lev_adv"));
                    data.setExpertLevel(object.getString("lev_exp"));
                    data.setMasterLevel(object.getString("lev_mas"));
                    if(!object.getString("lev_ult").equals("")){
                        data.setUltimaLevel(object.getString("lev_ult"));
                    }
                }
                QueryWrapper<ChunithmData> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("official_id",data.getOfficialId());
                if(getOne(queryWrapper) != null){
                    UpdateWrapper<ChunithmData> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("official_id",data.getOfficialId());
                    updateWrapper.set("is_enabled",data.getIsEnabled());
                    updateWrapper.set("basic_level",data.getBasicLevel());
                    updateWrapper.set("advanced_level",data.getAdvancedLevel());
                    updateWrapper.set("expert_level",data.getExpertLevel());
                    updateWrapper.set("master_level",data.getMasterLevel());
                    if(data.getUltimaLevel() != null){
                        updateWrapper.set("ultima_level",data.getUltimaLevel());
                    }
                    update(updateWrapper);
                }
                else {
                    save(data);
                }
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateFromDivingFish(List<JSONObject> list) {
        try {
            for (JSONObject object : list){
                JSONObject basicInfo = object.getJSONObject("basic_info");
                JSONArray level = object.getJSONArray("level");
                JSONArray constants = object.getJSONArray("ds");
                JSONArray charts = object.getJSONArray("charts");
                UpdateWrapper<ChunithmData> wrapper = new UpdateWrapper<>();
                wrapper.eq("official_id",object.getIntValue("id"));
                wrapper.set("is_enabled",true);
                wrapper.set("bpm",basicInfo.getString("bpm"));
                wrapper.set("version",basicInfo.getString("from"));
                if(level.size() < 6){
                    wrapper.set("basic_level",level.getString(0));
                    wrapper.set("advanced_level",level.getString(1));
                    wrapper.set("expert_level",level.getString(2));
                    wrapper.set("master_level",level.getString(3));
                    wrapper.set("basic_constant",constants.getFloat(0));
                    wrapper.set("advanced_constant",constants.getFloat(1));
                    wrapper.set("expert_constant",constants.getFloat(2));
                    wrapper.set("master_constant",constants.getFloat(3));
                    if(level.size() == 5){
                        wrapper.set("ultima_level",level.getString(4));
                        wrapper.set("ultima_constant",constants.getFloat(4));
                    }
                }
                wrapper.set("data_list",charts.toString());
                update(wrapper);
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCoverUrl() {
        try {
            List<Integer> idList = baseMapper.getOfficialIdList();
            for (Integer id : idList){
                if(id / 1000 == 8){
                    continue;
                }
                String builder = "CHU_UI_Jacket_" + String.format("%04d", id) + ".png";
                UpdateWrapper<ChunithmData> wrapper = new UpdateWrapper<>();
                wrapper.eq("official_id",id);
                wrapper.set("cover_url", builder);
                update(wrapper);
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ChunithmData getChunithmData(int officialId) {
        QueryWrapper<ChunithmData> wrapper = new QueryWrapper<>();
        wrapper.eq("official_id",officialId);
        return getOne(wrapper);
    }

    @Override
    public Map<Integer, String> getResultMap(String keyword) {
        Map<Integer, String> map = new TreeMap<>();
        QueryWrapper<ChunithmData> wrapper = new QueryWrapper<>();
        wrapper.like("LOWER(title)",keyword.toLowerCase()).or().like("LOWER(alias_list)",keyword.toLowerCase());
        List<ChunithmData> list = list(wrapper);
        for (ChunithmData data : list){
            Integer id = data.getOfficialId();
            String title = data.getTitle();
            if(id / 1000 == 8){
                title += "[" + data.getWeType() + "]";
            }
            if(!data.getIsEnabled()){
                title += "(*)";
            }
            map.put(id,title);
        }
        return map;
    }

    @Override
    public String getCoverUrl(int officialId) {
        return baseMapper.getCoverUrlByOfficialId(officialId);
    }
}
