import request from '@/utils/request'

export default {
  //获取会员分页列表
  getPageList(page, limit, searchObj) {
    return request({
      url: `/admin/core/userInfo/list/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //锁定和解锁
  lock(id, status) {
    return request({
      url: `/admin/core/userInfo/lock/${id}/${status}`,
      method: 'put'
    })
  },
  //获取会员登录日志列表
  getuserLoginRecordTop50(userId) {
    return request({
      url: `/admin/core/userLoginRecord/listTop50/${userId}`,
      method: 'get'
    })
  }
}