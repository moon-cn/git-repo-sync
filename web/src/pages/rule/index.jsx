import {Button, message, Modal, Popconfirm, Result, Skeleton} from 'antd';
import React from 'react';

import {ProTable} from "@ant-design/pro-components";
import {history} from "umi";
import hutool from "@moon-cn/hutool";

let api = '/api/rule/';


export default class extends React.Component {

  state = {

    formOpen: false,
    showEditForm: false,
    formValues: {},

  }
  actionRef = React.createRef();
  columns = [
    {
      title: '仓库1',
      dataIndex: ['repo1', 'url'],
      formItemProps: {
        rules: [{required: true}],
      }
    },
    {
      title: '分支',
      dataIndex: ['repo1', 'branch'],
    },

    {
      title: '仓库2',
      dataIndex: ['repo2', 'url'],
      formItemProps: {
        rules: [{required: true}],
      }
    },
    {
      title: '分支',
      dataIndex: ['repo2', 'branch'],
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
              formOpen: true,
              formValues: {
                repo1: { branch: 'master'},
                repo2: { branch: 'master'},
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
        title='规则信息'
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



