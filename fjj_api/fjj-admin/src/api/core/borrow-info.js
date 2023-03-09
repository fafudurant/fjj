import request from '@/utils/request'

export default {
  //借款信息列表
  getList(page,limit) {
    return request({
      url: `/admin/core/borrowInfo/list/${page}/${limit}`,
      method: 'get',
      
    })
  },
  //获取借款信息
  show(id) {
    return request({
      url: `/admin/core/borrowInfo/show/${id}`,
      method: 'get'
    })
  },
  //审批借款信息
  approval(borrowInfoApproval) {
    return request({
      url: '/admin/core/borrowInfo/approval',
      method: 'post',
      data: borrowInfoApproval
    })
  }
}