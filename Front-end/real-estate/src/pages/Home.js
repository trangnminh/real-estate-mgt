import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Carousel from 'react-elastic-carousel';
import HouseItemCard from '../components/HouseItemCard';
import HomeBanner from '../components/HomeBanner';
import Footer from '../components/Footer';
import Ads from '../components/Ads';

const Home = () => {

    const [houses, setHouses] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8080/api/v1/houses")
            .then((res) => res.json())
            .then((res) => {
                setHouses(res);
            });
    }, []);

    // New listing Carousel
    const breakPoints = [
        { width: 500, itemsToShow: 1 },
        { width: 768, itemsToShow: 2 },
        { width: 1200, itemsToShow: 3 },
        { width: 1500, itemsToShow: 4 }
    ]
    // box-shadow: 5px 10px 8px #888888
    return (
        <div>
            <HomeBanner />
            <Ads />
            <br />
            <br />
            <div style={{ position: "relative", width: "90%", padding: "10px 20px", margin: "0 auto", letterSpacing: "-.2px", boxShadow: "5px 10px 8px #888888" }}>
                <div style={{ paddingLeft: "110px" }}>
                    <h2>
                        New listing of Rental
                    </h2>
                    <Link to="/rental" >View all new listing</Link>
                </div>
                <br />

                <Carousel breakPoints={breakPoints}>
                    {houses.map((house) => (
                        <HouseItemCard key={house.houseId} houses={house} />
                    ))}
                </Carousel>
            </div>
            <br />

            {/* <Footer /> */}

        </div>
    );
};

export default Home;