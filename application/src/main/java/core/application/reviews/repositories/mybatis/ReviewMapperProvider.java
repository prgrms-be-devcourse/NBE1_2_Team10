package core.application.reviews.repositories.mybatis;

import org.apache.ibatis.jdbc.SQL;

public class ReviewMapperProvider {

    public String insertReview(){
        SQL query = new SQL()
                .INSERT_INTO("REVIEW_TABLE")
                .VALUES("review_id", "#{reviewId}")
                .VALUES("content", "#{content}")
                .VALUES("title", "#{title}")
                .VALUES("user_id", "#{userId}")
                .VALUES("like", "#{like}")
                .VALUES("created_at", "#{created_at}");

        return query.toString();
    }

    public String updateReview(){
        SQL query = new SQL()
                .UPDATE("REVIEW_TABLE")
                .SET("content = #{content}")
                .SET("title = #{title}")
                .SET("like_count = #{like}")
                .SET("updated_at = #{updated_at}")
                .WHERE("review_id = #{reviewId}");

        return query.toString();
    }

    public String deleteReview(){
        SQL query = new SQL()
                .DELETE_FROM("REVIEW_TABLE")
                .WHERE("review_id = #{reviewId}");

        return query.toString();
    }

    public String findByReviewId(){
        SQL query = new SQL()
                .SELECT("*")
                .FROM("REVIEW_TABLE")
                .WHERE("review_id = #{reviewId}");

        return query.toString();
    }


}
