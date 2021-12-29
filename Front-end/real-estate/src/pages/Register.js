import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router';
import RegisterForm from '../components/RegisterForm';

const Register = () => {

    const [users, setUsers] = useState([]);
    const [error, setError] = useState("");
    const [user, setUser] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8080/api/v1/users")
            .then((res) => res.json())
            .then((res) => {
                setUsers(res);
            });
    }, []);


    const Register = (details) => {
        console.log(details)
        users.forEach((u) => { // using forEach instead of map. because don't need to return something
            if (u.email === details.email) {
                console.log("email already exists")
                setError("email already exists");
            }
            else {
                setUser({
                    email: details.email,
                    password: details.password,
                    fullName: details.fullName,
                    phoneNumber: details.phoneNumber,
                    gender: details.gender,
                    dob: details.dob

                })
                fetch("http://localhost:8080/api/v1/users", {
                    method: `POST`,
                    headers: {
                        "Content-Type": "application/json; charset=utf-8"
                    },
                    body: JSON.stringify(user),
                })
                    .then((res) => {
                        res.json();
                    })
                    .then((res) => {
                        Navigate("/myPage")
                    })
            }
        })
    }

    return (
        <>
            <RegisterForm Register={Register} />
        </>
    );
};

export default Register;