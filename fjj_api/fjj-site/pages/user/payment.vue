<template>
  <div class="personal-main">
    <div class="personal-money">
      <div v-if="userType === 1" >
      <h3><i>回款记录</i></h3>
      <div class="personal-moneylist" style="margin-top: 40px;">
        <div class="pmain-contitle">
          <span class="pmain-title1 fb" style="width: 100px;">
            状态
          </span>
          <span class="pmain-title2 fb" style="width: 70px;">
            本息
          </span>
          <span class="pmain-title3 fb" style="width: 150px;">
            计划回款日期
          </span>
          <span class="pmain-title1 fb" style="width: 300px;">实际回款时间</span>
        </div>
        <ul>
          <li
            v-for="lendItemReturn in lendItemReturnList"
            :key="lendItemReturn.id"
            style="width:100%"
          >
            <span class="pmain-title1 pmain-height" style="width: 100px;">
              {{ lendItemReturn.status }}
            </span>~
            <span class="pmain-title2 pmain-height" style="width: 70px;">
              {{ lendItemReturn.total }}
            </span>
            <span class="pmain-title3 pmain-height" style="width: 150px;">
              {{ lendItemReturn.returnDate }}
            </span>
            <span
              :title="lendItemReturn.realReturnTime"
              class="pmain-title1 pmain-height"
              style="width: 500px; overflow: hidden;"
            >
              {{ lendItemReturn.realReturnTime }}
            </span>
          </li>
        </ul>
      </div>
      </div>

      <div v-if="userType === 2" >
      <h3><i>还款记录</i></h3>
      <div class="personal-moneylist" style="margin-top: 40px;">
        <div class="pmain-contitle">
          <span class="pmain-title1 fb" style="width: 200px;">
            还款批次号
          </span>
          <span class="pmain-title2 fb" style="width: 30px;">
            本息
          </span>
          <span class="pmain-title3 fb" style="width: 150px;">
            计划还款日期
          </span>
          <span class="pmain-title1 fb" style="width: 200px;">实际还款时间</span>
        </div>
        <ul>
          <li
            v-for="lendReturn in lendReturnList"
            :key="lendReturn.id"
            style="width:100%"
          >
            <span class="pmain-title1 pmain-height" style="width: 200px;">
              {{ lendReturn.returnNo }}
            </span>~
            <span class="pmain-title2 pmain-height" style="width: 30px;">
              {{ lendReturn.total }}
            </span>
            <span class="pmain-title3 pmain-height" style="width: 150px;">
              {{ lendReturn.returnDate }}
            </span>
            <span
              :title="lendReturn.realReturnTime"
              class="pmain-title1 pmain-height"
              style="width: 200px; overflow: hidden;"
            >
              {{ lendReturn.realReturnTime }}
            </span>
          </li>
        </ul>
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
      lendItemReturnList: [],
      lendReturnList: [],
    }
  },

  mounted() {
    let userInfo = cookie.get('userInfo')
    if (userInfo) {
      userInfo = JSON.parse(userInfo)
      this.userType = userInfo.userType
    }
    this.fetchlendItemReturnList()
    this.fetchlendReturnList()
  },

  methods: {
    fetchlendItemReturnList() {
      this.$axios.$get('/api/core/lendItemReturn/list').then((response) => {
        this.lendItemReturnList = response.data.list
      })
    },
    fetchlendReturnList() {
      this.$axios.$get('/api/core/lendReturn/list').then((response) => {
        this.lendReturnList = response.data.list
      })
    }
  }
}
</script>