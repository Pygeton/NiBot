package com.pygeton.nibot.repository.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public boolean updateFromJson(List<JSONObject> list) {
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
}
