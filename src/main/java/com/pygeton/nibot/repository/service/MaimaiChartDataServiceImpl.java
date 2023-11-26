package com.pygeton.nibot.repository.service;

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
                        case 1 -> data.setSongTitleKana(cell.getStringCellValue());
                        case 2 -> data.setSongType(cell.getStringCellValue());
                        case 3 -> data.setChartBasicRating(cell.getStringCellValue());
                        case 4 -> data.setChartBasicConstant((float)cell.getNumericCellValue());
                        case 5 -> data.setChartAdvancedRating(cell.getStringCellValue());
                        case 6 -> data.setChartAdvancedConstant((float)cell.getNumericCellValue());
                        case 7 -> data.setChartExpertRating(cell.getStringCellValue());
                        case 8 -> data.setChartExpertConstant((float)cell.getNumericCellValue());
                        case 9 -> data.setChartMasterRating(cell.getStringCellValue());
                        case 10 -> data.setChartMasterConstant((float)cell.getNumericCellValue());
                        case 11 -> data.setChartRemasterRating(cell.getStringCellValue());
                        case 12 -> data.setChartRemasterConstant((float)cell.getNumericCellValue());
                        case 13 -> data.setChartDataList(cell.getStringCellValue());
                        case 14 -> data.setOfficialId((int)cell.getNumericCellValue());
                        case 15 -> data.setChartStatusList(cell.getStringCellValue());
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
        return baseMapper.selectTitleAndOfficialId();
    }
}
