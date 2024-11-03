package com.hit.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UserReview {
    private String userId;

    private String review;
}
