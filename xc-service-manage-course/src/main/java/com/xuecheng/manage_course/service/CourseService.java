package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.FileSystemClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private FileSystemClient fileSystemClient;
    /**
     * 查找课程计划
     * @param courseId
     * @return : com.xuecheng.framework.domain.course.ext.TeachplanNode
     */
    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

    /**
     * 添加课程计划
     * @param teachplan
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //校验课程id和课程名称
        if(teachplan ==null|| StringUtils.isEmpty(teachplan.getCourseid())|| StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if(StringUtils.isEmpty(parentid)){
            //获取根节点
            parentid = this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //父节点
        Teachplan teachplanParent = optional.get();
        //父节点级别
        String parentGrade = teachplanParent.getGrade();
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");
        //子节点级别
        if(parentGrade.equals("1")){
            teachplan.setGrade("2");
        }else if(parentGrade.equals("2")){
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(teachplanParent.getCourseid());
        teachplanRepository.save(teachplan);

        return new ResponseResult(CommonCode.SUCCESS);
    }
    /**
     * //获取计划的根节点,如果没有则增加鞥节点
     * @param courseid
     * @return : java.lang.String
     */
    private String getTeachplanRoot(String courseid){
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if(!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        //取出计划的根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if(teachplanList ==null || teachplanList.size()==0){
            //新增一个节点
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseid);
            teachplan.setPname(courseBase.getName());
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        return teachplanList.get(0).getId();

    }

    /**
     *  分页查询课程列表
     * @param page
     * @param size
     * @param courseListRequest
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if(courseListRequest == null){
            courseListRequest = new CourseListRequest();
        }
        if(page<=0){
            page = 0;
        }
        if(size<=0 || size >=20){
            size =20 ;
        }
        PageHelper.startPage(page,size);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<>();
        courseInfoQueryResult.setList(courseListPage.getResult());
        courseInfoQueryResult.setTotal(courseListPage.getTotal());
        return new QueryResponseResult(CommonCode.SUCCESS,courseInfoQueryResult);
    }

    /**
     * 添加课程信息
     * @param courseBase
     * @return : com.xuecheng.framework.domain.course.response.AddCourseResult
     */
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        CourseBase course = courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS,course.getId());
    }

    /**
     * 根据课程id 查询课程
     * @param courseid
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    public CourseBase findCourseBaseById(String courseid) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            return courseBase;
        }
        return null;
    }

    /**
     * 更新课程基础信息
     * @param id
     * @param courseBase
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        CourseBase one = this.findCourseBaseById(id);
        if(one==null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        BeanUtils.copyProperties(courseBase,one);
        one.setId(id);
        courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据课程id 获取课程营销信息
     * @param courseId
     * @return : com.xuecheng.framework.domain.course.CourseMarket
     */
    public CourseMarket getCourseMarketById(String courseId) {
        if(courseId==null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 更新课程营销计划
     * @param id
     * @param courseMarket
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Transactional
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.getCourseMarketById(id);
        if(one!=null){
            BeanUtils.copyProperties(courseMarket,one);
            one.setId(id);
        }else{
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket,one);
            one.setId(id);
        }
        courseMarketRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 添加课程图片
     * @param courseId
     * @param pic
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        CoursePic coursePic = null;
        Optional<CoursePic> optionalPic = coursePicRepository.findById(courseId);
        if(optionalPic.isPresent()){
            coursePic = optionalPic.get();
        }
        if(coursePic==null){
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据id 查询图片
     * @param courseId
     * @return : com.xuecheng.framework.domain.course.CoursePic
     */
    public CoursePic findCoursePic(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 删除删除课程图片
     * @param courseId
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        //执行删除 返回1 表示删除成功 返回2 表示删除失败
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(!optional.isPresent()){
            return new ResponseResult(CommonCode.FAIL);
        }
        CoursePic coursePic = optional.get();
        String pic = coursePic.getPic();
        long result = coursePicRepository.deleteByCourseid(courseId);
        if(result>0){
            ResponseResult res = fileSystemClient.delete(pic);
            if(res.isSuccess()){
                return new ResponseResult(CommonCode.SUCCESS);
            }
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
