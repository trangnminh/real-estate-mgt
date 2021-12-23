import React from 'react';
import { Link } from 'react-router-dom';
import './SidebarNav.css';
import { IconContext } from 'react-icons';
import * as FaIcons from 'react-icons/fa';
import * as AiIcons from 'react-icons/ai';

const AdminSidebarNav = () => {
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
                            <Link to="/auth/admin/calendar">
                                <FaIcons.FaCalendarAlt />
                                <span>Calendar</span>
                            </Link>
                        </li>
                        <li className="nav-text">
                            <Link to="/auth/admin/addHouse">
                                <AiIcons.AiFillProfile />
                                <span>Add House</span>
                            </Link>
                        </li>

                        <li className="nav-text">
                            <Link to="/auth/admin/viewRentalHouses">
                                <FaIcons.FaClipboardList />
                                <span>Rental Houses</span>
                            </Link>
                        </li>

                        <li className="nav-text">
                            <Link to="/auth/admin/viewUsers">
                                <FaIcons.FaListOl />
                                <span>View Users</span>
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

export default AdminSidebarNav;