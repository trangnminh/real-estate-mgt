import React from 'react';
import { Carousel } from 'react-bootstrap';

const HomeBanner = () => {
    return (
        <Carousel >
            <Carousel.Item style={{ height: "300px" }}>
                <img
                    className="d-inline-block w-100 h-100"
                    src="https://hips.hearstapps.com/hmg-prod/images/home-alone-airbnb-01-exterior-credit-sarah-crowley-1638370433.jpg"
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