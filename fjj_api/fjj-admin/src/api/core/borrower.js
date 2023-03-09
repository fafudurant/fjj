import request from '@/utils/request'

export default {
  //获取借款人分页列表
  getPageList(page, limit, keyword) {
    return request({
      url: `/admin/core/borrower/list/${page}/${limit}`,
      method: 'get',
      params: {keyword}
    })
  },
  //获取借款人信息
  show(id) {
    return request({
      url: `/admin/core/borrower/show/${id}`,
      method: 'get'
    })
  },
  //借款额度审批
  approval(borrowerApproval) {
    return request({
      url: '/admin/core/borrower/approval',
      method: 'post',
      data: borrowerApproval
    })
  }
}