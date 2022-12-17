package com.example.fastdfstest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * CREATE TABLE tb_file(
 * 	id INT PRIMARY KEY auto_increment,
 * 	file_name VARCHAR(255) COMMENT '文件原始名',
 * 	group_name VARCHAR(32) COMMENT 'fdfs中存储器的卷名',
 * 	new_file_name VARCHAR(128) COMMENT 'fdfs中存储器内文件的名称，包括文件夹',
 * 	create_time datetime COMMENT '创建时间'
 * )ENGINE=INNODB;
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FdfsFile {
    private Integer id;
    private String fileName;
    private String groupName;
    private String newFileName;
    private Date createTime;
}
