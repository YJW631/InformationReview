package com.hit.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Author {
    private String puid;
    private String euid;
    private String puname;
    private String level;
    private String header;
    private String url;
}
