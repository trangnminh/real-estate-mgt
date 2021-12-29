import React, { useEffect, useState } from 'react';
import axios from 'axios'
import HomeBanner from '../components/HomeBanner';
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import MapSection from '../components/map/Map'
import Button from 'react-bootstrap/Button'
import '../App.css'
import { useNavigate } from 'react-router-dom';

const ViewDetail = () => {
    const bookMeeting = () => {
        navigate("/BookMeeting/" + house.houseId)
    }    
    const [house, setHouse] = useState([]);
    const [location, setLocation] = useState({});

    const navigate = useNavigate();
    useEffect(() => {
        const getData = async() => {
            const resp = await axios.get("http://localhost:8080/api/v1/house/2");
            console.log(resp)
            setHouse(resp.data);
            setLocation({
                address: resp.data.address,
                lat: resp.data.latitude,
                lng: resp.data.longitude
            })
        }
        getData();
    }, []);

    
    return (
        <div>
            <HomeBanner />
            <br />
            <br />
            <div style={{ position: "relative", width: "90%", padding: "10px 20px", margin: "0 auto", letterSpacing: "-.2px", boxShadow: "5px 10px 8px #888888" }}>
                <div style={{ paddingLeft: "110px" }}>
                    <Container>
                        <Row>
                            <h1>{house.name}</h1>
                        </Row>
                        <br/>
                        <Row>
                            <Col xs={6} md={4} lg={5}>
                                <img src={house.image} style={{ width: '30rem' }} ></img>
                            </Col>

                            <Col xs={6} md={5} lg={5}>
                                <Row>
                                    <h5>House Price: {house.price}</h5>
                                </Row>
                                <Row>
                                    <h5>Address: {house.address}</h5>
                                </Row>
                                <div style={{ paddingLeft: '5rem', width: '30rem' }}>
                                    <Row>
                                        <Button variant="primary" onClick={bookMeeting} style={{ height: '3rem' }}>Book A Meeting</Button>{' '}
                                    </Row>
                                    <br/>
                                    <Row>
                                        <Button variant="success" style={{ height: '3rem' }}>Deposit Money</Button>{' '}
                                    </Row>
                                </div>
                            </Col>
                            <br/>
                        </Row>
                        <br/>
                        <Row>
                        </Row>
                        <br/>
                        <Row>
                            <Col xs={6} md={4} lg={5}>
                                <h3>General Information</h3>
                                <Row>
                                    <Col><h5>Number of Beds: </h5></Col>
                                    <Col><h5>{house.numberOfBeds}</h5></Col>
                                </Row>
                                <Row>
                                    <Col><h5>Square Feets:</h5></Col>
                                    <Col><h5>{house.squareFeet}</h5></Col>
                                </Row>
                                <Row>
                                    <Col><h5>Type:</h5></Col>
                                    <Col><h5>{house.type}</h5></Col>
                                </Row>
                                <Row>
                                    <Col><h5>Status:</h5></Col>
                                    <Col><h5>{house.status}</h5></Col>
                                </Row>
                                <Row>
                                    <Col><h5>Description:</h5></Col>
                                    <h5>{house.description}</h5>
                                </Row>
                            </Col>
                            <Col xs={6} md={4} lg={5}  >
                                <h3>Location Information</h3>
                                <div className="map"></div>
                                <MapSection location={location} zoomLevel={17}/>
                            </Col>
                        </Row>
                    </Container>
                </div>
                <br />
            </div>
            <br />
           
        </div>
    );
};

export default ViewDetail;