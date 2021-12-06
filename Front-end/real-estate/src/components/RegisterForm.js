import React, { useState } from 'react';
import { Card, Col, Container, Row, Button, Form, FormGroup } from 'react-bootstrap';
import DatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'

const RegisterForm = () => {
    const [details, setDeatils] = useState({
        email: "", password: ""
    });

    const [selectedDOB, setSelectedDOB] = useState(null);

    const submitHandler = () => {
        fetch
            .then()
            .then()
    }

    return (
        <Container>
            <Row className="justify-content-md-center">
                <Col xs="10" md="9" lg="8" xl="7">
                    <Card className=" shadow border-0">
                        <Card.Header className="bg-transparent pb-5">
                            <div className="text-muted text-center mt-5 mb-3">
                                <small style={{ fontSize: "25px", color: "black" }}>Registration</small>
                            </div>
                        </Card.Header>
                        <Card.Body className="px-lg-5 py-lg-5">

                            <Form role="form">
                                <FormGroup className="mb-3">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control name="email"
                                        type="email"
                                        placeholder="Email"
                                        onChange={e => setDeatils({ ...details, email: e.target.value })}
                                        value={details.email}
                                        formNoValidate />
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        name="password"
                                        type="password"
                                        placeholder="Password"
                                        onChange={e => setDeatils({ ...details, password: e.target.value })}
                                        value={details.password}
                                        formNoValidate />
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Verify Password</Form.Label>
                                    <Form.Control
                                        name="verifyPassword"
                                        type="password"
                                        placeholder="Password"
                                        onChange={e => setDeatils({ ...details, verifyPassword: e.target.value })}
                                        value={details.password}
                                        formNoValidate />
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Full Name</Form.Label>
                                    <Form.Control name="fullName"
                                        placeholder="Full name"
                                        onChange={e => setDeatils({ ...details, fullName: e.target.value })}
                                        value={details.fullName}
                                        formNoValidate />
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Phone Number</Form.Label>
                                    <Form.Control
                                        name="phoneNumber"
                                        type="number"
                                        placeholder="Phone number"
                                        onChange={e => setDeatils({ ...details, phoneNumber: e.target.value })}
                                        value={details.phoneNumber}
                                        formNoValidate />
                                </FormGroup >

                                <Row>
                                    <Col>
                                        <FormGroup className="mb-3">
                                            <Form.Label>Gender</Form.Label>
                                            <Form.Select name="gender">
                                                <option value="M">M</option>
                                                <option value="F">F</option>
                                            </Form.Select>
                                        </FormGroup>
                                    </Col>
                                    <Col>
                                        <FormGroup className="mb-3">
                                            <Form.Label>Day of Birthday</Form.Label>
                                            <DatePicker
                                                selected={selectedDOB}
                                                onChange={date => setSelectedDOB(date)}
                                                maxDate={new Date()}
                                                isClearable
                                                showYearDropdown
                                                scrollableMonthYearDropdown
                                                showMonthDropdown
                                                scrollableYearDropdown />
                                        </FormGroup>
                                    </Col>
                                </Row>

                                <div className="text-center">
                                    <Button className="my-4" color="primary" type="button" onClick={submitHandler}>
                                        Register
                                    </Button>
                                </div>
                            </Form>

                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container >
    );
};

export default RegisterForm;