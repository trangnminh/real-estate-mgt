import React from 'react';
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

const AdsBanner = () => {
    const settings = {
        dots: true,
        infinite: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        speed: 500,
        autoplaySpeed: 3000,
        cssEase: "linear"
    };

    return (
        <Slider {...settings}>
            <div>
                <img className="d-inline-block w-100 h-100" src="https://hips.hearstapps.com/hmg-prod/images/home-alone-airbnb-01-exterior-credit-sarah-crowley-1638370433.jpg" alt="ads banner image1" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src="https://hips.hearstapps.com/hmg-prod/images/home-alone-airbnb-01-exterior-credit-sarah-crowley-1638370433.jpg" alt="ads banner image2" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src="https://hips.hearstapps.com/hmg-prod/images/home-alone-airbnb-01-exterior-credit-sarah-crowley-1638370433.jpg" alt="ads banner image3" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src="https://hips.hearstapps.com/hmg-prod/images/home-alone-airbnb-01-exterior-credit-sarah-crowley-1638370433.jpg" alt="ads banner image4" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src="https://hips.hearstapps.com/hmg-prod/images/home-alone-airbnb-01-exterior-credit-sarah-crowley-1638370433.jpg" alt="ads banner image5" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src="https://hips.hearstapps.com/hmg-prod/images/home-alone-airbnb-01-exterior-credit-sarah-crowley-1638370433.jpg" alt="ads banner image6" />
            </div>
        </Slider>
    );
};

export default AdsBanner;