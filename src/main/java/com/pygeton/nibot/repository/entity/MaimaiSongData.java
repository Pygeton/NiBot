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
    private String songTitleKana;
    private String songTitle;
    private String songArtist;
    private Integer songBpm;
    private String coverUrl;
    private String version;
    private String dxVersion;
    private String songGenre;
    private String remark;
}
