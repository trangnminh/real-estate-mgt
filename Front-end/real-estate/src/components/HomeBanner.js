import React from 'react';
import { Carousel } from 'react-bootstrap';
import banner from './../img/banner.jpg'

const HomeBanner = () => {
    return (
        <Carousel >
            <Carousel.Item style={{ height: "300px" }}>
                <img
                    className="d-inline-block w-100 h-100"
                    src={banner}
                    alt="carousel"
                />
                <Carousel.Caption>
                    <h1>Build Your Business Here</h1>
                    <h3>Reach to buyers, sellers and renters
                        on the largest real estate network on the web</h3>
                </Carousel.Caption>
            </Carousel.Item>
        </Carousel>
    );
};

export default HomeBanner;