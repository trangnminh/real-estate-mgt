import React, { useEffect, useState } from 'react';
import Login from './Login';
import Calendar from './Calendar';

const MyPage = () => {

    const [isLoggedIn, setIsLoggedIn] = useState(true);
    if (isLoggedIn) {
        return <Calendar />;
    }
    return (
        <>
            <Login />
        </>
    );
};

export default MyPage;