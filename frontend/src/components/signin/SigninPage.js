import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Button, Checkbox, Row, Col, Card } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import { useDispatch } from "react-redux";
import { loginUser } from "../../_actions/user_action";
import "./SignInPage.css";

function SignPagePage(props) {
  const dispatch = useDispatch();

  const [Email, setEmail] = useState("");
  const [Password, setPassword] = useState("");

  const onChangeEmail = (event) => {
    setEmail(event.currentTarget.value);
  };

  const onChangePassword = (event) => {
    setPassword(event.currentTarget.value);
  };

  const onClickSingin = (event) => {
    let body = {
      email: Email,
      password: Password,
    };

    //console.log("signi", body);

    if (Email && Password)
      dispatch(loginUser(body)).then((response) => {
        if (response.payload.loginSuccess) {
          props.history.push("/");
        } else {
          alert("Error !!");
        }
      });
  };

  const onSubmitSingin = (event) => {
    event.preventDefault();
  };

  return (
    <Row
      type="flex"
      justify="center"
      align="middle"
      style={{
        height: "100vh",
      }}
    >
      <Col>
        <Card className="login-form-card">
          <Form
            name="normal_login"
            className="login-form"
            initialValues={{ remember: true }}
            onSubmit={onSubmitSingin}
          >
            <Form.Item
              name="Email"
              rules={[{ required: true, message: "Please input your Email!" }]}
            >
              <Input
                type="email"
                value={Email}
                onChange={onChangeEmail}
                prefix={<UserOutlined className="site-form-item-icon" />}
                placeholder="Email"
              />
            </Form.Item>
            <Form.Item
              name="password"
              rules={[
                { required: true, message: "Please input your Password!" },
              ]}
            >
              <Input
                type="password"
                value={Email}
                onChange={onChangePassword}
                prefix={<LockOutlined className="site-form-item-icon" />}
                placeholder="Password"
              />
            </Form.Item>
            <Form.Item>
              <Form.Item name="remember" valuePropName="" noStyle>
                <Checkbox>Remember me</Checkbox>
              </Form.Item>

              <Link className="login-form-forgot" to="/signin">
                Forgot password
              </Link>
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                className="login-form-button"
                onClick={onClickSingin}
              >
                Sign in
              </Button>
              <Link to="/signup">Or register now!</Link>
            </Form.Item>
          </Form>
        </Card>
      </Col>
    </Row>
  );
}

export default SignPagePage;
