/**
 * UMI Runtime Config https://umijs.org/docs/runtime-config
 */
import {history} from 'umi';
import {message, Modal} from "antd";
import hutool from "@moon-cn/hutool";



hutool.http.setGlobalErrorMessageHandler(msg=>{
  message.error(msg)
})
hutool.http.axiosInstance.interceptors.response.use(rs=>{
  if(!rs.success){
    message.error(rs.message)
    return Promise.reject(rs.message)
  }
  return rs;
})

export function render(oldRender) {
  let path = history.location.pathname;
  if (path === '/login') {
    oldRender()
    return
  }

  hutool.http.get("api/login/check").then((rs) => {
    oldRender()
  }).catch(() => {
    history.push('/login')
    oldRender()
  })
}


export function onRouteChange({location, routes, action}) {

  Modal.destroyAll()
}

