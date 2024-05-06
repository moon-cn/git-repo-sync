import React from 'react';
import {Layout, Menu, message} from 'antd';
import {history} from 'umi';
import {
  ClusterOutlined, GithubOutlined,
  LogoutOutlined, OrderedListOutlined,
  ProjectOutlined,
  UserSwitchOutlined
} from "@ant-design/icons";
import hutool from "@moon-cn/hutool";

const {Content, Sider} = Layout;


export default class extends React.Component {

  state = {
    key: '/',
  };

  componentDidMount() {
    const loc = this.props.location;
    const {pathname} = loc
    const key = pathname.substr(1)

    this.setState({key})
  }

  logout = () => {
    localStorage.clear();
    const hide = message.loading("注销登录...", 0)
    hutool.http.get("/api/logout").then(rs => {
      history.push("/login")
      hide()
    })

  };

  render() {
    // 权限过滤
    let items = [

      {
        key: 'rule',
        label: '规则',
        icon: <OrderedListOutlined style={{color: 'gold'}}/>,
      },
      {
        key: 'credential',
        label: '凭据',
        icon: <GithubOutlined style={{color: 'green'}}/>
      },
      {
        key: 'user',
        label: '用户设置',
        icon: <UserSwitchOutlined style={{color: 'darkmagenta'}}/>,
      },
      {
        key: 'logout',
        label: '退出',
        icon: <LogoutOutlined style={{color: 'rebeccapurple'}}/>
      },
    ]



    return (
      <Layout style={{minHeight: 'calc(100vh - 20px)'}}>
        <Sider width={150}>

          <div className='py-3 m-4  bg-gray-800 flex align-middle justify-center '>
            <a className='text-white' onClick={() => history.push('/')}>GIT SYN</a>
          </div>
          <Menu theme='dark'
                onClick={this.onClick} selectedKeys={[this.state.key]} items={items}/>
        </Sider>

        <Content className='pl-2'>
          {this.props.children}
        </Content>
      </Layout>
    );

  }

  onClick = ({key}) => {
    this.setState({key})
    if (key === 'logout') {
      this.logout()
      return
    }
    history.push('/' + key)
  }


}


