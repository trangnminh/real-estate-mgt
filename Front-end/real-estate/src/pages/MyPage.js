import React, { useEffect, useState } from 'react';
import decode from 'jwt-decode';
import Login from '../components/Login';
import Calendar from './Calendar';

const MyPage = () => {

    const [isLoggedIn, setIsLoggedIn] = useState(true);

    return (
        <React.Fragment>
            {isLoggedIn ? (<Calendar />) : (<Login />)}
        </React.Fragment>
    );
};

export default MyPage;