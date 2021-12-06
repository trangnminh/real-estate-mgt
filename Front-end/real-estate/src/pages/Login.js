import React, { useState } from 'react';
import decode from 'jwt-decode';
import axios from 'axios';
import LoginForm from '../components/LoginForm';
import LoginFormError from '../components/LoginFormError';

const Login = () => {

    const adminUser = {
        email: "admin@admin.com",
        password: "admin1234"
    }

    const [user, setUser] = useState({ email: "", password: "", })
    const [error, setError] = useState(false);

    const Login = (details) => {
        console.log(details)
        if (details.email === adminUser.email && details.password === adminUser.password) {
            console.log("Logged In")
            setUser({
                email: details.email,
                password: details.password
            })
        } else {
            setError(true);
            console.log("error : " + error);

        }
    }

    return (
        <>
            <LoginForm Login={Login} error={error} />
            <LoginFormError show={error} onHide={() => setError(false)} />
        </>
    );
};

export default Login;