package com.pygeton.nibot.repository.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.communication.entity.mai.MaimaiChartStat;
import com.pygeton.nibot.communication.entity.mai.MaimaiRecChart;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import com.pygeton.nibot.repository.mapper.MaimaiChartDataMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class MaimaiChartDataServiceImpl extends ServiceImpl<MaimaiChartDataMapper, MaimaiChartData> implements IMaimaiChartDataService{

    @Override
    public boolean init() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("mai/maimai_chart.xlsx");
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(Objects.requireNonNull(stream));
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                if(row.getRowNum() == 0){
                    continue;
                }
                Iterator<Cell> cellIterator = row.cellIterator();
                MaimaiChartData data = new MaimaiChartData();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex){
                        case 0 -> data.setId((int)cell.getNumericCellValue());
                        case 1 -> data.setTitleKana(cell.getStringCellValue());
                        case 2 -> data.setType(cell.getStringCellValue());
                        case 3 -> data.setBasicLevel(cell.getStringCellValue());
                        case 4 -> data.setBasicConstant((float)cell.getNumericCellValue());
                        case 5 -> data.setAdvancedLevel(cell.getStringCellValue());
                        case 6 -> data.setAdvancedConstant((float)cell.getNumericCellValue());
                        case 7 -> data.setExpertLevel(cell.getStringCellValue());
                        case 8 -> data.setExpertConstant((float)cell.getNumericCellValue());
                        case 9 -> data.setMasterLevel(cell.getStringCellValue());
                        case 10 -> data.setMasterConstant((float)cell.getNumericCellValue());
                        case 11 -> data.setRemasterLevel(cell.getStringCellValue());
                        case 12 -> data.setRemasterConstant((float)cell.getNumericCellValue());
                        case 13 -> data.setDataList(cell.getStringCellValue());
                        case 14 -> data.setOfficialId((int)cell.getNumericCellValue());
                        case 15 -> data.setStatList(cell.getStringCellValue());
                        case 16 -> data.setIsNew(cell.getNumericCellValue() == 1);
                    }
                }
                save(data);
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MaimaiChartData> getTitleAndOfficialIdList() {
        return baseMapper.getTitleAndOfficialId();
    }

    @Override
    public boolean updateChartData(List<JSONObject> list) {
        try {
            for (JSONObject object : list){
                JSONObject basicInfo = object.getJSONObject("basic_info");
                JSONArray level = object.getJSONArray("level");
                JSONArray constants = object.getJSONArray("ds");
                JSONArray charts = object.getJSONArray("charts");
                UpdateWrapper<MaimaiChartData> wrapper = new UpdateWrapper<>();
                wrapper.eq("title_kana",baseMapper.getTitleKanaFromSongData(object.getString("title")));
                wrapper.eq("type",object.getString("type"));
                wrapper.set("official_id",object.getIntValue("id"));
                wrapper.set("is_new",basicInfo.getBoolean("is_new"));
                wrapper.set("version",basicInfo.getString("from"));
                wrapper.set("basic_level",level.getString(0));
                wrapper.set("advanced_level",level.getString(1));
                wrapper.set("expert_level",level.getString(2));
                wrapper.set("master_level",level.getString(3));
                wrapper.set("basic_constant",constants.getFloat(0));
                wrapper.set("advanced_constant",constants.getFloat(1));
                wrapper.set("expert_constant",constants.getFloat(2));
                wrapper.set("master_constant",constants.getFloat(3));
                if(level.size() > 4){
                    wrapper.set("remaster_level",level.getString(4));
                    wrapper.set("remaster_constant",constants.getFloat(4));
                }
                wrapper.set("data_list",charts.toString());
                wrapper.set("stat_list",null);
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
    public boolean updateStatList(JSONObject charts) {
        try {
            for (Map.Entry<String,Object> entry : charts.entrySet()){
                String key = entry.getKey();
                JSONArray values = (JSONArray) entry.getValue();
                JSONObject expStat = values.getJSONObject(2);
                JSONObject masStat = values.getJSONObject(3);
                JSONObject reMasStat = values.getJSONObject(4);
                MaimaiChartStat chartStat = new MaimaiChartStat();
                int expCount = expStat.getIntValue("cnt");
                JSONArray expDist = expStat.getJSONArray("dist");
                chartStat.setExpSSPlus(expDist.getDoubleValue(11) / expCount);
                chartStat.setExpSSS(expDist.getDoubleValue(12) / expCount);
                chartStat.setExpSSSPlus(expDist.getDoubleValue(13) / expCount);
                int masCount = masStat.getIntValue("cnt");
                JSONArray masDist = masStat.getJSONArray("dist");
                chartStat.setMasSSPlus(masDist.getDoubleValue(11) / masCount);
                chartStat.setMasSSS(masDist.getDoubleValue(12) / masCount);
                chartStat.setMasSSSPlus(masDist.getDoubleValue(13) / masCount);
                if(!reMasStat.isEmpty()){
                    int reMasCount = reMasStat.getIntValue("cnt");
                    JSONArray reMasDist = reMasStat.getJSONArray("dist");
                    chartStat.setReMasSSPlus(reMasDist.getDoubleValue(11) / reMasCount);
                    chartStat.setReMasSSS(reMasDist.getDoubleValue(12) / reMasCount);
                    chartStat.setReMasSSSPlus(reMasDist.getDoubleValue(13) / reMasCount);
                }
                UpdateWrapper<MaimaiChartData> wrapper = new UpdateWrapper<>();
                wrapper.eq("official_id",key);
                wrapper.set("stat_list", JSON.toJSONString(chartStat));
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
    public MaimaiChartData getChartData(int officialId) {
        QueryWrapper<MaimaiChartData> wrapper = new QueryWrapper<>();
        wrapper.eq("official_id",officialId);
        return getOne(wrapper);
    }

    @Override
    public List<Integer> getOfficialId(String titleKana) {
        return baseMapper.getOfficialIdByTitleKana(titleKana);
    }

    @Override
    public List<MaimaiChartData> getChartDataListByVersion(String version) {
        QueryWrapper<MaimaiChartData> wrapper = new QueryWrapper<>();
        if(version.equals("ALL")){
            wrapper.eq("version","maimai")
                    .or().eq("version","maimai PLUS")
                    .or().eq("version","maimai GreeN")
                    .or().eq("version","maimai GreeN PLUS")
                    .or().eq("version","maimai ORANGE")
                    .or().eq("version","maimai ORANGE PLUS")
                    .or().eq("version","maimai PiNK")
                    .or().eq("version","maimai PiNK PLUS")
                    .or().eq("version","maimai MURASAKi")
                    .or().eq("version","maimai MURASAKi PLUS")
                    .or().eq("version","maimai MiLK")
                    .or().eq("version","maimai MiLK PLUS")
                    .or().eq("version","maimai FiNALE");
        }
        else if(version.equals("maimai")){
            wrapper.eq("version",version)
                    .ne("official_id",70)
                    .or().eq("version","maimai PLUS");
        }
        else {
            wrapper.eq("version",version);
        }
        return list(wrapper);
    }

    @Override
    public List<MaimaiRecChart> getRecChartList(float constant,boolean isNew) {
        return baseMapper.getRecChartByConstant(constant,isNew);
    }
}
