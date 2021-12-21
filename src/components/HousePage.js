import React from 'react';
import { Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import HouseItemCard from '../components/HouseItemCard';

const HousePage = ({ houses }) => {
    return (
        <Container>
            <Row>
                {houses.map((house) => (
                    <Col sm>
                    <HouseItemCard key={house.houseId} houses={house} />
                    </Col>
                ))}
            </Row>
        </Container>
    );
};

export default HousePage;