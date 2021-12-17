import React, { useEffect, useState } from 'react';
import axios from 'axios'
import HomeBanner from '../components/HomeBanner';
import Carousel from 'react-elastic-carousel';
import HouseItemCard from '../components/HouseItemCard';
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Image from 'react-bootstrap/Image'
import MapSection from '../components/map/Map'
import '../App.css'
const location = {
    address: '1600 Amphitheatre Parkway, Mountain View, california.',
    lat: 37.42216,
    lng: -122.08427,
  } // our location object from earlier

const ViewDetail = () => {
    const [house, setHouse] = useState([]);
    
    useEffect(() => {
        axios.get("http://localhost:8081/api/v1/house/2")
            .then((res) => {
                setHouse(res.data);
            });
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
                            <Col xs={6} md={4}>
                                <Row>
                                    <h1>{house.name}</h1>
                                </Row>
                                <img src={house.image} fluid style={{ width: '40rem' }} ></img>
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
                                    <h5>House Price: {house.price}</h5>
                                </Row>
                                <Row>
                                    <h5>Address: {house.address}</h5>
                                </Row>
                                <Row>
                                    <h5>Number of Beds: {house.numberOfBeds}</h5>
                                </Row>
                                <Row>
                                    <h5>Square Feets: {house.squareFeet}</h5>
                                </Row>
                                <Row>
                                    <h5>Type: {house.type}</h5>
                                </Row>
                                <Row>
                                    <h5>Description:</h5>
                                    <h5>{house.description}</h5>
                                </Row>
                            </Col>
                            <Col xs={6} md={4} lg={5}  >
                                <h3>Location Information</h3>
                                <MapSection location={location} zoomLevel={17} />
                            </Col>
                        </Row>
                    </Container>
                </div>
                <br />
                {/* <Carousel>
                    {house.map((house) => (
                        <HouseItemCard key={house.houseId} houses={house} />
                    ))}
                </Carousel> */}
            </div>
            <br />
           
        </div>
    );
};

export default ViewDetail;