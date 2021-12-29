import React, { useEffect, useState } from 'react';
import UserSidebarNav from '../components/UserSidebarNav';
import ProfileForm from '../components/ProfileForm';

const Profile = () => {

    return (
        <>
            <UserSidebarNav />
            <ProfileForm />
        </>
    );
};

export default Profile;