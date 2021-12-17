import React, { useEffect, useState } from 'react';
import HomeBanner from '../components/HomeBanner';
import axios from 'axios'
import PaginationBar from '../components/PaginationBar';
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import HouseItemCard from '../components/HouseItemCard';
import Form from 'react-bootstrap/Form'
const Rental = () => {
    const [houses, setHouses] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:8081/api/v1/houses")
            .then((res) => {
                setHouses(res.data);
            });
    }, []);


    return (
        <div>
            <HomeBanner />
            <br />
            <br />
            <div style={{ position: "relative", width: "90%", padding: "10px 20px", margin: "0 auto", letterSpacing: "-.2px", boxShadow: "5px 10px 8px #888888" }}>
                <div style={{ paddingLeft: "110px" }}>
                    <h2>
                        View All House Listing
                    </h2>
                    <div>
                        <Container>
                            <Row>
                                <Col xs={2} md={2} lg={3}>
                                    <Form.Select aria-label="Default select example">
                                        <option>Open this select menu</option>
                                        <option value="1">One</option>
                                        <option value="2">Two</option>
                                        <option value="3">Three</option>
                                    </Form.Select>
                                </Col>
                                <Col xs={2} md={2} lg={3}>
                                    <Form.Select aria-label="Default select example">
                                        <option>Open this select menu</option>
                                        <option value="1">One</option>
                                        <option value="2">Two</option>
                                        <option value="3">Three</option>
                                    </Form.Select>
                                </Col>
                                <Col xs={2} md={2} lg={3}>
                                    <Form.Select aria-label="Default select example">
                                        <option>Open this select menu</option>
                                        <option value="1">One</option>
                                        <option value="2">Two</option>
                                        <option value="3">Three</option>
                                    </Form.Select>
                                </Col>
                                <Col xs={2} md={2} lg={3}>
                                    <Form.Control type="text" placeholder="Search Bar" />
                                </Col>
                            </Row>
                        </Container>
                    </div>
                    <br/>
                    <Container>
                        <Row>
                            {houses.map((house) => (
                                <Col xs={2} md={2} lg={4}>
                                    <HouseItemCard key={house.houseId} houses={house} />
                                </Col>
                            ))}
                        </Row>
                        <br />
                        <Row>
                            <PaginationBar/>
                        </Row>
                    </Container>
                </div>
            </div>
            <br/>
        </div>
    );
};

export default Rental;