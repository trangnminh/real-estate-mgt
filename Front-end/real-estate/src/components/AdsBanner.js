import React from 'react';
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import img5 from './../img/5.jpg'
import img6 from './../img/6.jpg'
import img7 from './../img/7.jpg'
import img8 from './../img/8.jpg'
import img9 from './../img/9.jpg'
import img10 from './../img/10.jpg'


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
                <img className="d-inline-block w-100 h-100" src={img5} alt="ads banner image1" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src={img6} alt="ads banner image2" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src={img7} alt="ads banner image3" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src={img8} alt="ads banner image4" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src={img9} alt="ads banner image5" />
            </div>
            <div>
                <img className="d-inline-block w-100 h-100" src={img10} alt="ads banner image6" />
            </div>
        </Slider>
    );
};

export default AdsBanner;