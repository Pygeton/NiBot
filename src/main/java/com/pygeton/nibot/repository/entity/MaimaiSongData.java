package com.pygeton.nibot.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("maimai_song_data")
public class MaimaiSongData {

    private Integer id;
    private String song_title_kana;
    private String song_title;
    private String song_artist;
    private Integer song_bpm;
    private String jacket_url;
    private String version;
    private String dx_version;
    private String song_genre;
    private String remark;
}
