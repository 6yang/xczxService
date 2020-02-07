package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachplanRepository extends JpaRepository<Teachplan,String> {
    /**
     * 根据课程id和父节点查询出节点列表
     * @param courseid
     * @param parentid
     * @return : java.util.List<com.xuecheng.framework.domain.course.Teachplan>
     */
    List<Teachplan> findByCourseidAndParentid(String courseid,String parentid);
}
