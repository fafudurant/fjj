module.exports = {
  server: {
    port: 3000, // default: 3000
  },

  head: {
    title: 'FAFU基金',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      {
        hid: 'meta-key-words',
        name: 'keywords',
        content:
          'FAFU基金官网，网络投资平台，解决个人小额借款，短期借款问题，资金银行存管，安全保障。',
      },
      {
        hid: 'description',
        name: 'description',
        content:
          'FAFU基金官网，网络投资平台，解决个人小额借款，短期借款问题，资金银行存管，安全保障。',
      },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
  },

  css: [
    // CSS file in the project
    '~/assets/css/common.css',
  ],

  modules: [
    '@nuxtjs/axios', //引入axios模块
  ],

  env: {
    BASE_API: 'http://localhost',
  },

  axios: {
    // Axios options here
    baseURL: 'http://localhost',
  },

  plugins: [
    '~/plugins/axios',
    '~/plugins/element-ui.js',
    '~/plugins/vue-qriously-plugin.js',
  ],

  // ssr: false, //设置为false表示客户端渲染，true为服务器端渲染，默认为true
}
