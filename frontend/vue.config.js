const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new (require('webpack')).DefinePlugin({
        '__VUE_PROD_HYDRATION_MISMATCH_DETAILS__': JSON.stringify(true),
      }),
    ],
  },
  devServer: {
    open: true,
    port: 8081,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // spring boot 서버 주소
        changeOrigin: true
      },
    }
  }
});
