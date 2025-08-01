package com.zwt.repo.sqlite.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Date: 2024/5/13 17:17
 */
@Data
@Entity
@Table(name = "proxy_ip_record")
public class ProxyIpRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private String ip;
    private Integer port;
    private String user;
    private String pw;
    private String originalData;
    private Date expireTime;
    private Integer status;
    private String supplierCode;
    private Date ctime;
    private Date utime;
}
