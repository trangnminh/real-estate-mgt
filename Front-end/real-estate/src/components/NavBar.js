import React from 'react';
import { Nav, Navbar } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const NavBar = () => {

    return (
        <>
            <br />
            <br />
            <br />
            <Navbar collapseOnSelect expand="lg" bg="white" variant="white">
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="justify-content-center" style={{ flex: 1, padding: '20px', alignItems: 'center' }}>

                        <Nav.Item>
                            <Nav.Link href="/" className="text-dark" style={{ fontSize: "20px" }}>Home</Nav.Link>
                        </Nav.Item>
                        <Nav.Item style={{ marginLeft: "50px", marginRight: "50px" }}>
                            <Nav.Link href="/rental" className="text-dark" style={{ fontSize: "20px" }}>Rental</Nav.Link>
                        </Nav.Item>

                        <Nav.Item style={{ marginLeft: "50px", marginRight: "50px" }}>
                            <Link to="/" />
                            <img
                                src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdHgXe64oYzHr-6QiG5DKZ67ECAxOTnjDbfieFKMfzybHXXNmmAr-8jYzT70o8Dlv2uRY&usqp=CAU"
                                alt="logo"
                                width="150"
                                height="150"
                                className="d-inline-block align-top"
                            />
                        </Nav.Item>

                        <Nav.Item style={{ marginLeft: "50px", marginRight: "50px" }}>
                            <Nav.Link href="/help" className="text-dark" style={{ fontSize: "20px" }}>Help</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/myPage" className="text-dark" style={{ fontSize: "20px" }}>My page</Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        </>
    );
};

export default NavBar;