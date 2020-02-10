package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {

   @Select("select cb.*," +
           " (select pic from course_pic where cb.id = courseid ) pic from course_base cb ")
   Page<CourseInfo> findCourseListPage(CourseListRequest courseListRequest);
}
