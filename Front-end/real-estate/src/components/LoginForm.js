import React, { useState } from 'react';
import { Card, Col, Container, Row, Button, Form, FormGroup } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import Google from "../img/google.png";
import { GoogleLogin, GoogleLogout } from 'react-google-login';
import FacebookLogin from 'react-facebook-login';
require('dotenv').config()

const LoginForm = ({ Login }) => {
    const client_id = process.env.OAUTH2_CLIENT_ID
    const facebook_app_id = process.env.FACEBOOK_APP_ID

    // Google log in
    const onLoginSuccess = (res) => {
        console.log('Login Success:', res.profileObj);
    };

    const onLoginFailure = (res) => {
        console.log('Login Failed:', res);
    };

    // Facebook log in
    const [login, setLogin] = useState(false);
    const [data, setData] = useState({});

    const responseFacebook = (response) => {
        console.log(response);
        if (response.accessToken) {
            setLogin(true);
        } else {
            setLogin(false);
        }
    }

    const [details, setDeatils] = useState({ email: "", password: "" });
    const submitHandler = (e) => {
        e.preventDefault();
        Login(details);
    }

    return (
        <Container>
            <br />
            <br />
            <Row className="justify-content-md-center">
                <Col xs="10" md="9" lg="8" xl="7">
                    <Card className=" shadow border-0">
                        <Card.Header className="bg-transparent pb-5">
                            <div className="text-muted text-center mt-2 mb-3">
                                <small>Sign in with</small>
                            </div>
                            <div className="btn-wrapper text-center">

                                <GoogleLogin
                                    clientId={client_id}
                                    onSuccess={onLoginSuccess}
                                    onFailure={onLoginFailure}
                                    cookiePolicy={'single_host_origin'}
                                    isSignedIn={true}
                                    render={renderProps => (
                                        <Button onClick={renderProps.onClick}
                                            style={{ backgroundColor: "red", height: "51px", width: "160px", fontSize: "18px", fontWeight: "bold" }}>
                                            <img src={Google} alt="" className="icon" style={{ marginRight: "20px" }} />
                                            GOOGLE
                                        </Button>
                                    )}
                                />

                                <FacebookLogin
                                    appId={facebook_app_id}
                                    fields="name,email"
                                    size="small"
                                    callback={responseFacebook}
                                    icon="fa-facebook-square"
                                    textButton="FaceBook"
                                />

                            </div>
                        </Card.Header>
                        <Card.Body className="px-lg-5 py-lg-5">
                            <div className="text-center text-muted mb-4">
                                <small>Or sign in with credentials</small>
                            </div>

                            <Form role="form">
                                <FormGroup className="mb-3">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control name="email"
                                        type="email"
                                        placeholder="Email"
                                        onChange={e => setDeatils({ ...details, email: e.target.value })}
                                        value={details.name}
                                        formNoValidate />
                                </FormGroup>

                                <FormGroup>
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        name="password"
                                        type="password"
                                        placeholder="Password"
                                        onChange={e => setDeatils({ ...details, password: e.target.value })}
                                        value={details.password}
                                        formNoValidate />
                                </FormGroup>
                                <div className="text-center">
                                    <Button className="my-4" color="primary" type="button" onClick={submitHandler}>
                                        Sign in
                                    </Button>
                                </div>
                            </Form>

                        </Card.Body>
                    </Card>

                    <Row className="mt-3">
                        <Col className="text-center ml-4">
                            <Link to="/auth/register" className="text-light">
                                <small style={{ color: "black" }}>Forgot password?</small>
                            </Link>
                        </Col>
                        <Col className="text-center mr-4">
                            <Link to="/register" className="text-light">
                                <small style={{ color: "black" }}>Create new account</small>
                            </Link>
                        </Col>
                    </Row>

                </Col>
            </Row>
        </Container >
    );
};

export default LoginForm;