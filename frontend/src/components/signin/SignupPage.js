import React, { useState } from "react";
import { Form, Input, Button, Row, Col, Card } from "antd";
import { useDispatch } from "react-redux";
import { signupUser } from "../../_actions/signin_action";

function SignupPage(props) {
  const dispatch = useDispatch();
  const [form] = Form.useForm();
  const formItemLayout = {
    labelCol: {
      xs: { span: 24 },
      sm: { span: 8 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 16 },
    },
  };

  const tailFormItemLayout = {
    wrapperCol: {
      xs: {
        span: 24,
        offset: 0,
      },
      sm: {
        span: 16,
        offset: 8,
      },
    },
  };

  const [Company, setCompany] = useState("");
  const [Email, setEmail] = useState("");
  const [Password, setPassword] = useState("");
  const [Mobile, setMobile] = useState("");
  const [Name, setName] = useState("");

  const onChangeCompany = (event) => {
    setCompany(event.currentTarget.value);
  };

  const onChangeEmail = (event) => {
    setEmail(event.currentTarget.value);
  };

  const onChangePassword = (event) => {
    setPassword(event.currentTarget.value);
  };

  const onChangeName = (event) => {
    setName(event.currentTarget.value);
  };

  const onChangeMobile = (event) => {
    setMobile(event.currentTarget.value);
  };

  const onClickSingup = (event) => {
    let body = {
      company: Company,
      email: Email,
      password: Password,
      name: Name,
      cellphone: Mobile,
    };
    console.log(body);
    if (Email && Password && Name && Mobile)
      dispatch(signupUser(body)).then((response) => {
        if (response.payload) {
          props.history.push("/signin");
        } else {
          alert("Error !!");
        }
      });
  };

  // const onSubmitSingin = (event) => {
  //   event.preventDefault();
  // };

  return (
    <Row
      type="flex"
      justify="center"
      align="middle"
      style={{
        height: "100vh",
      }}
    >
      <Col span={6}>
        <Card className="login-form-card">
          <Form
            {...formItemLayout}
            form={form}
            name="register"
            // onFinish={onFinish}
            scrollToFirstError
          >
            <Form.Item
              name="company"
              label="Company"
              rules={[
                {
                  type: "text",
                  message: "The input is not valid company!",
                },
                {
                  required: true,
                  message: "Please input your company!",
                },
              ]}
            >
              <Input
                type="text"
                value={Company}
                onChange={onChangeCompany}
                placeholder="Company"
              />
            </Form.Item>

            <Form.Item
              name="email"
              label="E-mail"
              rules={[
                {
                  type: "email",
                  message: "The input is not valid E-mail!",
                },
                {
                  required: true,
                  message: "Please input your E-mail!",
                },
              ]}
            >
              <Input
                type="email"
                value={Email}
                onChange={onChangeEmail}
                placeholder="E-mail"
              />
            </Form.Item>

            <Form.Item
              name="password"
              label="Password"
              rules={[
                {
                  required: true,
                  message: "Please input your password!",
                },
              ]}
              hasFeedback
            >
              <Input.Password
                placeholder="Password"
                onChange={onChangePassword}
              />
            </Form.Item>

            <Form.Item
              name="confirm"
              label="Confirm Password"
              dependencies={["password"]}
              hasFeedback
              rules={[
                {
                  required: true,
                  message: "Please confirm your password!",
                },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue("password") === value) {
                      return Promise.resolve();
                    }
                    return Promise.reject(
                      new Error(
                        "The two passwords that you entered do not match!"
                      )
                    );
                  },
                }),
              ]}
            >
              <Input.Password placeholder="Confirm Password" />
            </Form.Item>

            <Form.Item
              name="name"
              label="Name"
              //tooltip="What do you want others to call you?"
              rules={[
                {
                  required: true,
                  message: "Please input your name!",
                },
              ]}
            >
              <Input
                type="text"
                value={Name}
                onChange={onChangeName}
                placeholder="Name"
              />
            </Form.Item>

            <Form.Item
              name="mobile"
              label="Mobile phone"
              rules={[
                {
                  required: true,
                  message: "Please input your mobile phone!",
                  whitespace: true,
                },
              ]}
            >
              <Input
                type="text"
                value={Mobile}
                onChange={onChangeMobile}
                placeholder="Mobile Phone"
              />
            </Form.Item>
            <Form.Item {...tailFormItemLayout}>
              <Button
                type="primary"
                htmlType="submit"
                className="login-form-button"
                onClick={onClickSingup}
              >
                Sign up
              </Button>
            </Form.Item>
          </Form>
        </Card>
      </Col>
    </Row>
  );
}

export default SignupPage;
