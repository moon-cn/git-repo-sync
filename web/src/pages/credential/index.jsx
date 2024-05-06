import {Button, message, Modal, Popconfirm, Result, Skeleton} from 'antd';
import React from 'react';

import {ProTable} from "@ant-design/pro-components";
import hutool from "@moon-cn/hutool";

let api = '/api/credential/';


export default class extends React.Component {

  state = {

    formOpen: false,
    showEditForm: false,
    formValues: {},

  }
  actionRef = React.createRef();
  columns = [
    {
      title: 'url',
      dataIndex: 'url',
    },

    {
      title: '账号',
      dataIndex: 'username',
    },

    {
      title: '密码',
      dataIndex: 'password',
      valueType: 'password'
    },

    {
      title: 'ssh',
      dataIndex: 'ssh',
      valueType: 'text'
    },


    {
      title: '最近更新',
      dataIndex: 'modifyTime',
      sorter: true,
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, row) => {
        return <>
          <Button
            type='link'
            onClick={() => {
              this.setState({
                formOpen: true,
                formValues: row
              })
            }}>编辑</Button>
          &nbsp;
          <Popconfirm title="确定删除，删除后将不可恢复"
                      onConfirm={() => this.handleDelete(row)}>
            <a>删除</a>
          </Popconfirm>

        </>
      },
    },
  ];




  handleSave = value => {
    value.id = this.state.formValues.id
    hutool.http.post(api + 'save', value).then(rs => {
      this.state.formOpen = false;
      this.setState(this.state)
      this.reload();
    })
  }

  reload = () => {
    this.actionRef.current.reload();
  };
  handleDelete = (row) => {
    hutool.http.get(api + 'delete', {id: row.id}).then(rs => {
      message.info(rs.message)
      this.actionRef.current.reload();
    })
  }


  render() {
    let {formOpen} = this.state



    return (<>
      <ProTable
        actionRef={this.actionRef}
        search={false}
        toolBarRender={(action, {selectedRows}) => [
          <Button type="primary" onClick={() => {
            this.setState({
              formOpen: true, formValues: {
                branch: 'master',
                dockerfile: 'Dockerfile'
              }
            })
          }}>
            新增
          </Button>,
        ]}
        request={(params, sort) => hutool.http.requestAntdSpringPageData(api + "list", params, sort)}
        columns={this.columns}
        rowSelection={false}
        rowKey="id"
        bordered={true}
        options={{search: true}}

      />


      <Modal
        maskClosable={false}
        destroyOnClose
        title='凭证信息'
        open={formOpen}
        onCancel={() => {
          this.setState({formOpen: false})
        }}
        footer={null}
      >
        <ProTable
          type='form'
          form={{
            initialValues: this.state.formValues,
            layout: 'horizontal',
            labelCol: {flex: '100px'},
          }}
          onSubmit={this.handleSave}
          columns={this.columns}
        />
      </Modal>
    </>)
  }


}



