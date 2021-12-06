import React from 'react';
import { Card } from 'react-bootstrap';
import { Navigate } from 'react-router';

const AdsCategoryCard = ({ url, city }) => {

    const viewHouse = () => {
        Navigate("/rental/" + city)
    }

    return (
        <Card className="bg-dark text-white text-center" style={{ width: '25rem', height: "17rem" }}>
            <Card.Img className="d-inline-block w-100 h-100" src={url} alt="Card image" />
            <Card.ImgOverlay>
                <Card.Title style={{ fontSize: "50px" }}>{city}</Card.Title>
            </Card.ImgOverlay>
        </Card>
    );
};

export default AdsCategoryCard;