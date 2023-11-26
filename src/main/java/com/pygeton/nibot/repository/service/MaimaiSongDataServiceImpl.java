package com.pygeton.nibot.repository.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pygeton.nibot.repository.entity.MaimaiChartData;
import com.pygeton.nibot.repository.entity.MaimaiSongData;
import com.pygeton.nibot.repository.mapper.MaimaiSongDataMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class MaimaiSongDataServiceImpl extends ServiceImpl<MaimaiSongDataMapper, MaimaiSongData> implements IMaimaiSongDataService{

    @Override
    public boolean init() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("mai/maimai_song.xlsx");
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
                MaimaiSongData data = new MaimaiSongData();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex){
                        case 0 -> data.setId((int)cell.getNumericCellValue());
                        case 1 -> data.setSongTitleKana(cell.getStringCellValue());
                        case 2 -> data.setSongTitle(cell.getStringCellValue());
                        case 3 -> data.setSongArtist(cell.getStringCellValue());
                        case 4 -> data.setSongBpm(Integer.valueOf(cell.getStringCellValue()));
                        case 5 -> data.setCoverUrl(cell.getStringCellValue());
                        case 6 -> data.setVersion(cell.getStringCellValue());
                        case 7 -> data.setRemark(cell.getStringCellValue());
                        case 8 -> data.setSongGenre(cell.getStringCellValue());
                        case 9 -> data.setDxVersion(cell.getStringCellValue());
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
    public boolean updateCoverUrl(List<MaimaiChartData> list) {
        try {
            for(MaimaiChartData chartData : list){
                if(chartData != null){
                    String coverUrl;
                    if (chartData.getOfficialId() == null){
                        continue;
                    }
                    else if(chartData.getOfficialId() == 0){
                        continue;
                    }
                    else if(chartData.getOfficialId() >= 10000){
                        coverUrl = "/mai/" + chartData.getOfficialId() + ".png";
                    }
                    else{
                        coverUrl = "/mai/" + String.format("%05d",chartData.getOfficialId()) + ".png";
                    }
                    UpdateWrapper<MaimaiSongData> wrapper = new UpdateWrapper<>();
                    wrapper.set("cover_url",coverUrl);
                    wrapper.eq("song_title_kana",chartData.getSongTitleKana());
                    update(wrapper);
                }
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
