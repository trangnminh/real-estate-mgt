import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import LoginFormError from '../components/LoginFormError';

const Login = () => {

    const navigate = useNavigate();

    const adminUser = {
        email: "admin@admin.com",
        password: "admin1234"
    }

    // get all users
    const [users, setUsers] = useState([]);
    const fetchHouses = async () => {
        try {
            const response = await axios.get(
                'http://localhost:8080/api/v1/users'
            );
            setUsers(response.data);
        } catch (e) {

        }
    };
    useEffect(() => {
        fetchHouses();
    }, []);


    const [user, setUser] = useState({ email: "", password: "" })
    const [error, setError] = useState(false);

    // get details data from LoginForm.js and handle error check
    const Login = (details) => {
        users.forEach((u) => { // using forEach instead of map. because don't need to return data
            if (u.email === details.email && u.password === details.password) {
                console.log("Logged In")
                setUser({
                    email: details.email,
                    password: details.password
                })
                navigate("/auth/calendar")
            } else if (details.email === adminUser.email && details.password === adminUser.password) {
                console.log("Logged In")
                setUser({
                    email: details.email,
                    password: details.password
                })
                navigate("/auth/calendar")
            } else {
                setError(true);
                console.log("error : " + error);
            }
        })
    }

    return (
        <>
            <LoginForm Login={Login} />
            <LoginFormError show={error} onHide={() => setError(false)} />
        </>
    );
};

export default Login;