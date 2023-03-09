import request from '@/utils/request'
export default {
  //根据上级id获取子节点数据列表
  listByParentId(parentId) {
    return request({
      url: `/admin/core/dict/listByParentId/${parentId}`,
      method: 'get'
    })
  }
}