import {Button, Col, Form, Input, message, Modal, Popconfirm, Result, Row, Select, Skeleton, Tag} from 'antd';
import React from 'react';

import {ProTable} from "@ant-design/pro-components";
import hutool from "@moon-cn/hutool";

let api = '/api/rule/';


export default class extends React.Component {

  state = {

    formOpen: false,
    showEditForm: false,
    formValues: {},


    credentialOption: [],

  }
  actionRef = React.createRef();
  columns = [
    {
      title: '仓库1',
      dataIndex: ['repo1', 'url'],
      render(_, r) {
        return <>{r.repo1.url} &nbsp;
          <Tag>{r.repo1.branch}</Tag>
        </>
      }
    },

    {
      title: '仓库2',
      dataIndex: ['repo2', 'url'],
      render(_, r) {
        return <>{r.repo1.url} &nbsp;
          <Tag>{r.repo1.branch}</Tag>
        </>
      }
    },


    {
      title: '上次同步',
      dataIndex: 'lastTime',
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, row) => {
        return <>
          <Button
            size='small'
            onClick={() => {
              this.sync(row.id)
            }}>立即同步</Button>
          &nbsp;
          <Button             size='small'

                              onClick={() => {
              this.setState({
                formOpen: true,
                formValues: row
              })
            }}>编辑</Button>
          &nbsp;
          <Popconfirm title="确定删除，删除后将不可恢复"
                      onConfirm={() => this.handleDelete(row)}>
            <Button size='small'>删除</Button>
          </Popconfirm>

        </>
      },
    },
  ];


  componentDidMount() {
    hutool.http.get("/api/credential/options").then(rs => {
      this.setState({credentialOption: rs.data})
    })
  }

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


  formRef = React.createRef()

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
                repo1: {branch: 'master'},
                repo2: {branch: 'master'},
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
        width={800}
        title='规则信息'
        open={formOpen}
        onCancel={() => {
          this.setState({formOpen: false})
        }}
        onOk={() => this.formRef.current.submit()}
      >

        <Form ref={this.formRef} layout='vertical' onFinish={this.handleSave} initialValues={this.state.formValues}>
          <Row gutter={4}>
            <Col span={16}>
              <Form.Item label='仓库地址' name={['repo1', 'url']} rules={[{required: true}]}>
                <Input/>
              </Form.Item>
            </Col>
            <Col span={4}>
              <Form.Item label='分支' name={['repo1', 'branch']} rules={[{required: true}]} >
                <Input/>
              </Form.Item>
            </Col>

          </Row>

          <Row gutter={4}>
            <Col span={16}>
              <Form.Item label='仓库地址' name={['repo2', 'url']} rules={[{required: true}]}>
                <Input/>
              </Form.Item>
            </Col>
            <Col span={4}>
              <Form.Item label='分支' name={['repo2', 'branch']} rules={[{required: true}]} >
                <Input/>
              </Form.Item>
            </Col>
          </Row>
        </Form>


      </Modal>
    </>)
  }


  sync = id => {
    let hide = message.loading('同步中....', 0);
    hutool.http.post('api/rule/sync', {id}).then(rs => {
      message.success(rs.message)
      this.reload()
    }).finally(hide)

  };
}



