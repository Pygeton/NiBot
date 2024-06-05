package com.pygeton.nibot.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("maimai_song_data")
public class MaimaiSongData {

    //@TableId(type = IdType.AUTO)
    private Integer id;
    private String titleKana;
    private String title;
    private String artist;
    private Integer bpm;
    private String coverUrl;
    private String genre;
    private String remark;
    private String aliasList;
}
