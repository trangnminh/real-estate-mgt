import React, { useState } from 'react';
import { Card, Col, Container, Row, Button, Form, FormGroup } from 'react-bootstrap';
import DatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'

const RegisterForm = ({ Register }) => {
    const [details, setDeatils] = useState({
        email: "",
        password: "",
        verifyPassword: "",
        fullName: "",
        phoneNumber: "",
        gender: "M",
        dob: ""
    });
    const [showMessage, setShowMessage] = useState(false);
    const [formerrors, setFormErrors] = useState({});

    const validate = () => {
        let errors = {};

        //email field
        if (!details.email) {
            errors.email = "Email address is required";
        } else if (!/\S+@\S+\.\S+/.test(details.email)) {
            errors.email = "Email address is invalid";
        }

        //password field
        if (!details.password) {
            errors.password = "Password is required";
        } else if (details.password.length < 8) {
            errors.password = "Password must be more than 8 digits";
        }

        //verifyPassword field
        if (!details.verifyPassword) {
            errors.verifyPassword = "Password is required";
        } else if (details.password.length < 8) {
            errors.verifyPassword = "Password must be more than 8 digits";
        }

        //compare passwords field
        if (details.password !== details.verifyPassword) {
            errors.verifyPassword = "Password is unmatched. Please check again"
        }

        //name field
        if (!details.fullName) {
            errors.fullName = "Full name is required";
        }

        //phoneNumber field
        if (!details.phoneNumber) {
            errors.phoneNumber = "Phone number is required";
        } else if (details.phoneNumber.length < 9) {
            errors.phoneNumber = "Phone number is very short. Please check again";
        }

        //dob field
        if (!details.dob) {
            errors.dob = "Day of birthday is required";
        }

        setFormErrors(errors);

        if (Object.keys(errors).length === 0) {
            return true;
        } else {
            return false;
        }
    };

    const submitHandler = (e) => {

        e.preventDefault();
        if (validate(details)) {
            setShowMessage(true);
            Register(details);
        } else {
            setShowMessage(false);
        }
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
                                        required />
                                    {formerrors.email && (
                                        <p className="text-danger">{formerrors.email}</p>
                                    )}
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        name="password"
                                        type="password"
                                        placeholder="Password"
                                        onChange={e => setDeatils({ ...details, password: e.target.value })}
                                        value={details.password}
                                        required />
                                    {formerrors.password && (
                                        <p className="text-danger">{formerrors.password}</p>
                                    )}
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Verify Password</Form.Label>
                                    <Form.Control
                                        name="verifyPassword"
                                        type="password"
                                        placeholder="Verify Password"
                                        onChange={e => setDeatils({ ...details, verifyPassword: e.target.value })}
                                        value={details.verifyPassword}
                                        required />
                                    {formerrors.verifyPassword && (
                                        <p className="text-danger">{formerrors.verifyPassword}</p>
                                    )}
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Full Name</Form.Label>
                                    <Form.Control name="fullName"
                                        placeholder="Full name"
                                        onChange={e => setDeatils({ ...details, fullName: e.target.value })}
                                        value={details.fullName}
                                        required />
                                    {formerrors.fullName && (
                                        <p className="text-danger">{formerrors.fullName}</p>
                                    )}
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Phone Number</Form.Label>
                                    <Form.Control
                                        name="phoneNumber"
                                        type="number"
                                        placeholder="Phone number"
                                        onChange={e => setDeatils({ ...details, phoneNumber: e.target.value })}
                                        value={details.phoneNumber}
                                        required />
                                    {formerrors.phoneNumber && (
                                        <p className="text-danger">{formerrors.phoneNumber}</p>
                                    )}
                                </FormGroup >

                                <Row>
                                    <Col>
                                        <FormGroup className="mb-3">
                                            <Form.Label>Gender</Form.Label>
                                            <Form.Select name="gender" onChange={e => setDeatils({ ...details, gender: e.target.value })}>
                                                <option value="M">M</option>
                                                <option value="F">F</option>
                                            </Form.Select>
                                        </FormGroup>
                                    </Col>
                                    <Col>
                                        <FormGroup className="mb-3">
                                            <Form.Label>Day of Birthday</Form.Label>
                                            <DatePicker
                                                required
                                                selected={details.dob}
                                                maxDate={new Date()}
                                                dateFormat='yyyy-MM-dd'
                                                name="dob"
                                                isClearable
                                                showYearDropdown
                                                scrollableMonthYearDropdown
                                                showMonthDropdown
                                                scrollableYearDropdown
                                                onChange={date => setDeatils({ ...details, dob: date })} />
                                            {formerrors.dob && (
                                                <p className="text-danger">{formerrors.dob}</p>
                                            )}
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