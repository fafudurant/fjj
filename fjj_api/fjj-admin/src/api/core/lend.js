import request from '@/utils/request'

export default {
  //标的列表
  getList(page,limit) {
    return request({
      url: `/admin/core/lend/list/${page}/${limit}`,
      method: 'get'
    })
  },
  //获取标的信息
  show(id) {
    return request({
      url: `/admin/core/lend/show/${id}`,
      method: 'get'
    })
  },
  //放款
  makeLoan(id) {
    return request({
      url: `/admin/core/lend/makeLoan/${id}`,
      method: 'get'
    })
  }
}