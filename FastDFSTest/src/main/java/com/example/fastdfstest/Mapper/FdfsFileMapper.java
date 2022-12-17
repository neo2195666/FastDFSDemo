package com.example.fastdfstest.Mapper;

import com.example.fastdfstest.pojo.FdfsFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
@Transactional
public interface FdfsFileMapper {

    @Select("select id, file_name as fileName,group_name as groupName,new_file_name as newFileName,create_time as createTime from tb_file where id = #{id} ")
    FdfsFile selectById(Integer id);

    @Delete("delete from tb_file where id = #{id}")
    int delete(Integer id);

    @Insert("insert into tb_file(id,file_name,group_name,new_file_name,create_time)"
            + "values (#{id},#{fileName},#{groupName},#{newFileName},#{createTime})")
    int insert(FdfsFile file);

    //@Select("select * from tb_file")
    @Select("select id, file_name as fileName,group_name as groupName,new_file_name as newFileName,create_time as createTime from tb_file")
    List<FdfsFile> selectAll();

}
