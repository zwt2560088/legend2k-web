package com.zwt.repo.sqlite.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Date: 2024/5/13 17:17
 */
@Data
@Entity
@Table(name = "used_phone_num")
public class UsedPhoneNum implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNum;

    private String competitorCode;

    private Long ctime;

}
