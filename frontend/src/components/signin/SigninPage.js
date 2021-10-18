import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Button, Checkbox, Row, Col, Card } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import { useDispatch } from "react-redux";
import { signinUser } from "../../_actions/signin_action";
import { withRouter } from "react-router-dom";
import { Layout } from "antd";
import Cookies from "js-cookie";
import "./SignInPage.css";

function SignPagePage(props) {
  const dispatch = useDispatch();
  const { Header } = Layout;
  const [Email, setEmail] = useState("");
  const [Password, setPassword] = useState("");
  const [form] = Form.useForm();

  useEffect(() => {
    // 브라우저 API를 이용하여 문서 타이틀을 업데이트합니다.
    document.title = `David's Na Order System`;
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const onChangeEmail = (event) => {
    setEmail(event.currentTarget.value);
  };

  const onChangePassword = (event) => {
    setPassword(event.currentTarget.value);
  };

  const onClickSingin = (event) => {
    let param = {
      email: Email,
      password: Password,
    };

    if (Email && Password && Password.length > 3) {
      dispatch(signinUser(param))
        .then((response) => {
          if (response.payload.isAuth) {
            let token = response.payload.token;
            let refresh= response.payload.refresh;
            localStorage.setItem("jwtToken", token);
            Cookies.set("refresh", refresh);
            props.history.push("/order");
          } else {
            form.resetFields();
            alert("input Error !!");
          }
        })
        .catch((error) => {
          form.resetFields();
          alert("Error !!");
        });
    }
  };

  const onSubmitSingin = (event) => {
    event.preventDefault();
  };

  return (
    <div style={{ backgroundColor: "#f0f0f0" }}>
      <Header
        style={{
          position: "fixed",
          zIndex: 1,
          width: "100%",
          backgroundColor: '#333',
        }}
      >
        <h2 style={{  color: "#fff", textAlign: "center" }}>The place your Dollar Shop Business starts!</h2>
      </Header>
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
              form={form}
              name="normal_login"
              className="login-form"
              initialValues={{ remember: false }}
              onSubmit={onSubmitSingin}
            >
              <Form.Item
                name="Email"
                rules={[
                  { required: true, message: "Please input your Email!" },
                ]}
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
                  {
                    required: true,
                    min: 3,
                    message: "Password must be minimum 3 characters.",
                  },
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
      <Header
        style={{
          position: "absolute",
          zIndex: 1,
          width: "100%",
          bottom: "0",
          backgroundColor: '#333',
        }}
      >
        <h2 style={{ color: "#fff", textAlign: "center" }}>
          If you have any enquiries, Please Contact David Na 027-652-1111, davidna@xtra.co.nz
        </h2>
      </Header>
    </div>
  );
}

export default withRouter(SignPagePage);
