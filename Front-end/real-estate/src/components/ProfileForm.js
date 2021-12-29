import React, { useEffect, useState } from 'react';
import { Card, Col, Container, Row, Button, Form, FormGroup } from 'react-bootstrap';
import DatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import { useNavigate, useParams } from 'react-router-dom';

const ProfileForm = () => {

    const { id } = useParams();
    const navigate = useNavigate();
    const [user, setUser] = useState([]);

    // useEffect(() => {
    //     fetch("http://localhost:8080/api/v1/users" + id)
    //         .then((res) => res.json())
    //         .then((res) => {
    //             setUser(res);
    //         }); // asynchronous function
    // }, []);

    return (
        <Container>
            <br />
            <br />
            <br />
            <Row className="justify-content-md-center">
                <Col xs="10" md="9" lg="8" xl="7">
                    <Card className=" shadow border-0">
                        <Card.Header className="bg-transparent pb-5">
                            <div className="text-muted text-center mt-5 mb-3">
                                <small style={{ fontSize: "25px", color: "black" }}>Profile</small>
                            </div>
                        </Card.Header>
                        <Card.Body className="px-lg-5 py-lg-5">
                            <Form role="form">
                                <FormGroup className="mb-3">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control name="email"
                                        type="email"
                                        placeholder="Email"
                                        value={user.email} />
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Full Name</Form.Label>
                                    <Form.Control name="fullName"
                                        placeholder="Full name"
                                        value={user.fullName}
                                    />
                                </FormGroup>

                                <FormGroup className="mb-3">
                                    <Form.Label>Phone Number</Form.Label>
                                    <Form.Control
                                        name="phoneNumber"
                                        type="number"
                                        placeholder="Phone number"
                                        value={user.phoneNumber}
                                    />
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
                                                required
                                                selected={user.dob}
                                                maxDate={new Date()}
                                                dateFormat='yyyy-MM-dd'
                                                name="dob"
                                                isClearable
                                                showYearDropdown
                                                scrollableMonthYearDropdown
                                                showMonthDropdown
                                                scrollableYearDropdown
                                            />
                                        </FormGroup>
                                    </Col>
                                </Row>

                                <div className="text-center">
                                    <div className='row'>
                                        <Button className="my-4" color="primary" type="button">
                                            Change Password
                                        </Button>
                                        <Button className="my-4" color="primary" type="button">
                                            Edit Profile
                                        </Button>
                                    </div>
                                </div>
                            </Form>

                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container >
    );
};

export default ProfileForm;