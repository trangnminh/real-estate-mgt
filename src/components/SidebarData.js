import React from 'react';
import * as FaIcons from 'react-icons/fa';
import * as AiIcons from 'react-icons/ai';
import * as IoIcons from 'react-icons/io';

export const SidebarData = [
    {
        title: 'Calendar',
        path: '/auth/calendar',
        icon: <FaIcons.FaCalendarAlt />,
        cName: 'nav-text'
    },
    {
        title: 'Profile',
        path: '/auth/profile',
        icon: <AiIcons.AiFillProfile />,
        cName: 'nav-text'
    },
    {
        title: 'Support',
        path: '/support',
        icon: <IoIcons.IoMdHelpCircle />,
        cName: 'nav-text'
    }
];