import React from 'react';
import { Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const HouseItemCard = ({ houses }) => {
    const navigate = useNavigate();

    const viewDeatil = () => {
        navigate("/viewDeatil/" + houses.houseId)
    }

    return (
        <Card onClick={viewDeatil} style={{ width: '20rem' }}>
            <Card.Img variant="top" src={houses.image} />
            <Card.Body>
                <Card.Title>{houses.name}</Card.Title>
                <Card.Text>{houses.price + " VND"}</Card.Text>
                <Card.Text>{houses.numberOfBeds + " bed(s)"}</Card.Text>
                <Card.Text>{houses.address}</Card.Text>
            </Card.Body>
        </Card>
    );
};

export default HouseItemCard;