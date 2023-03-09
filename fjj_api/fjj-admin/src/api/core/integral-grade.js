import request from '@/utils/request'

const integral_grade_name = '/admin/core/integralGrade'
export default{
  //获取积分等级列表
  list(){
    return request({
      url: `${integral_grade_name}/list`,
      method: 'get'
    })
  },
  //根据id删除积分等级
  removeById(id){
    return request({
      url: `${integral_grade_name}/remove/${id}`,
      method: 'delete'
    })
  },
  //新增积分等级
  save(integralGrade){
    return request({
      url: `${integral_grade_name}/save`,
      method: 'post',
      data: integralGrade
    })
  },
  //根据id获取积分等级
  getById(id){
    return request({
      url: `${integral_grade_name}/get/${id}`,
      method: 'get'
    })
  },
  //更新积分等级
  updateById(integralGrade){
    return request({
      url: `${integral_grade_name}/update`,
      method: 'put',
      data: integralGrade
    })
  }
}