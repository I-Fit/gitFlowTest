const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
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
