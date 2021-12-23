import React from 'react';
import { Link } from 'react-router-dom';
import './SidebarNav.css';
import { IconContext } from 'react-icons';
import * as FaIcons from 'react-icons/fa';
import * as AiIcons from 'react-icons/ai';

const UserSidebarNav = () => {
    return (
        <>
            <IconContext.Provider value={{ color: '#fff' }}>
                <nav className='nav-menu active'>
                    <ul className='nav-menu-items' >
                        <li className='navbar-toggle'>
                            <Link to='#' className='menu-bars'>
                            </Link>
                        </li>
                        <li className="nav-text">
                            <Link to="/auth/calendar">
                                <FaIcons.FaCalendarAlt />
                                <span>Calendar</span>
                            </Link>
                        </li>
                        <li className="nav-text">
                            <Link to="/auth/profile">
                                <AiIcons.AiFillProfile />
                                <span>Profile</span>
                            </Link>
                        </li>

                        <li className="nav-text">

                        </li>
                        <li className="nav-text">
                            <Link to="">
                                <FaIcons.FaSignOutAlt />
                                <span>Log Out</span>
                            </Link>
                        </li>
                    </ul>
                </nav>
            </IconContext.Provider>
        </>
    );
};

export default UserSidebarNav;