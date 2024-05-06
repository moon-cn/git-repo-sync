import React from "react";
import {Button, Card, Form, Input, message} from "antd";
import hutool from "@moon-cn/hutool";

export default class extends React.Component {

  handleChangePwd = values=>{
    hutool.http.postForm('api/user/changePwd',values).then(rs=>{
      message.success(rs.message)
    })
  }
  render() {
    return <Card>
      <h3>修改密码</h3>
      <Form layout='inline' onFinish={this.handleChangePwd}>
        <Form.Item name='pwd' label='新密码' rules={[{required:true}]}>
          <Input.Password/>
        </Form.Item>
        <Button type='primary' htmlType='submit'>确定</Button>
      </Form>
    </Card>
  }
}
