package core.ckeditorwithgooglecloud.repositories;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostingContentMapper {

    @Insert("INSERT INTO posting_content_table (title, content) VALUES (#{title}, #{content}) ")
    @Options(useGeneratedKeys = true, keyProperty = "postingId")
    int save(PostingContentEntity postingContentEntity);

    @Select("SELECT * FROM posting_content_table ")
    @Results(id = "postingContentEntity", value = @Result(column = "posting_id", property = "postingId"))
    List<PostingContentEntity> selectAll();

    @Select("SELECT * FROM posting_content_table WHERE posting_id = #{postingId} ")
    @ResultMap("postingContentEntity")
    Optional<PostingContentEntity> selectById(Long postingId);

    @Update("UPDATE posting_content_table "
            + "SET title = #{title}, content = #{content} "
            + "WHERE posting_id = #{postingId} ")
    int update(PostingContentEntity postingContentEntity);

    @Delete("DELETE FROM posting_content_table WHERE posting_id = #{postingId} ")
    void deleteById(Long postingId);
}
