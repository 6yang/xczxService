package com.xuecheng.manage_course.controller;


import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;


    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachPlanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    /**
     * 添加课程计划
     * @param teachplan
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody  Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    /**
     * 查询课程列表
     * @param page
     * @param size
     * @param courseListRequest
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourseList(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            CourseListRequest courseListRequest) {

        return courseService.findCourseList(page,size,courseListRequest);
    }

    /**
     * 添加课程基本信息
     * @param courseBase
     * @return : com.xuecheng.framework.domain.course.response.AddCourseResult
     */
    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    /**
     * 根据id查询课程
     * @param courseid
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    @Override
    @GetMapping("/get/{id}")
    public CourseBase findCourseBaseById(@PathVariable("id") String id) {
        return courseService.findCourseBaseById(id);
    }

    /**
     * 更新课程基础信息
     * @param id
     * @param courseBase
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Override
    @PutMapping("/coursebase/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id, @RequestBody CourseBase courseBase) {
        return courseService.updateCourseBase(id,courseBase);
    }

    /**
     * 根据课程id 获取课程营销信息
     * @param courseId
     * @return : com.xuecheng.framework.domain.course.CourseMarket
     */
    @Override
    @GetMapping("/coursemarket/get/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    /**
     * 更新课程营销计划
     * @param id
     * @param courseMarket
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Override
    @PostMapping("/coursemarket/update/{id}")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id, @RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(id,courseMarket);
    }

    /**
     * 添加课程图片
     * @param courseId
     * @param pic
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId,
                                       @RequestParam("pic") String pic) {
        return courseService.addCoursePic(courseId,pic);
    }

    /**
     * 根据id查询图片
     * @param courseId
     * @return : com.xuecheng.framework.domain.course.CoursePic
     */
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePic(courseId);
    }

    /**
     * 删除课程图片
     * @param courseId
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }
}
