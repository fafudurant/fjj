<template>
<div>
  <div class="personal-main">
    <div class="personal-money">
      <div v-if="userType === 1" >
        <h3><i>投资记录</i></h3>
        <div class="personal-moneylist" style="margin-top: 40px;">
          <div class="pmain-contitle">
            <span class="pmain-title1 fb" style="width: 100px;">
              投资人
            </span>
            <span class="pmain-title2 fb" style="width: 70px;">
              投资金额
            </span>
            <span class="pmain-title3 fb" style="width: 150px;">
              投资时间
            </span>
            <span class="pmain-title1 fb" style="width: 300px;">投资编号</span>
          </div>
          <ul>
            <li
              v-for="lendItem in lendItemList"
              :key="lendItem.id"
              style="width:100%"
            >
              <span class="pmain-title1 pmain-height" style="width: 100px;">
                {{ lendItem.investName }}
              </span>~
              <span class="pmain-title2 pmain-height" style="width: 70px;">
                {{ lendItem.investAmount }}
              </span>
              <span class="pmain-title3 pmain-height" style="width: 150px;">
                {{ lendItem.investTime }}
              </span>
              <span
                :title="lendItem.lendItemNo"
                class="pmain-title1 pmain-height"
                style="width: 500px; overflow: hidden;"
              >
                {{ lendItem.lendItemNo }}
              </span>
            </li>
          </ul>
        </div>
      </div>
      <div v-if="userType === 2" >
        <h3><i>借款记录</i></h3>
        <div class="personal-moneylist" style="margin-top: 40px;">
          <div class="pmain-contitle">
            <span class="pmain-title1 fb" style="width: 100px;">
              借款年利率
            </span>
            <span class="pmain-title2 fb" style="width: 70px;">
              借款金额
            </span>
            <span class="pmain-title3 fb" style="width: 150px;">
              借款时间
            </span>
          </div>
          <ul>
            <li
              v-for="borrowInfo in borrowInfoList"
              :key="borrowInfo.id"
              style="width:100%"
            >
              <span class="pmain-title1 pmain-height" style="width: 100px;">
                {{ borrowInfo.borrowYearRate }}
              </span>~
              <span class="pmain-title2 pmain-height" style="width: 70px;">
                {{ borrowInfo.amount }}
              </span>
              <span class="pmain-title3 pmain-height" style="width: 150px;">
                {{ borrowInfo.createTime }}
              </span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import cookie from 'js-cookie'
export default {
  data() {
    return {
      userType: 0,
      lendItemList: [],
      borrowInfoList: []
    }
  },

  mounted() {
    let userInfo = cookie.get('userInfo')
    if (userInfo) {
      userInfo = JSON.parse(userInfo)
      this.userType = userInfo.userType
    }
    this.fetchLendItemList()
    this.fetchBorrowInfoList()
  },

  methods: {
    fetchLendItemList() {
      this.$axios.$get('/api/core/lendItem/list').then((response) => {
        this.lendItemList = response.data.list
      })
    },
    fetchBorrowInfoList() {
      this.$axios.$get('/api/core/borrowInfo/list').then((response) => {
        this.borrowInfoList = response.data.list
      })
    }
  }
}
</script>
