import React from 'react';
import { Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const HouseItemCard = ({ houses }) => {
    const navigate = useNavigate();

    const viewDeatil = () => {
        navigate("/detail/" + houses.houseId)
    }

    return (
        <Card onClick={viewDeatil} style={{ width: '20rem' }}>
            <Card.Img variant="top" src={houses.image[3]} />
            <Card.Body>
                <Card.Title>{houses.name}</Card.Title>
                <Card.Text>{houses.price + " USD"}</Card.Text>
                <Card.Text>{houses.numberOfBeds + " bed(s)"}</Card.Text>
                <Card.Text>{houses.squareFeet + " square feet"}</Card.Text>
                <Card.Text>{houses.address}</Card.Text>
            </Card.Body>
        </Card>
    );
};

export default HouseItemCard;