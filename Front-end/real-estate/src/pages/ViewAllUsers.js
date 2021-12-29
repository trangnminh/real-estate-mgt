import React from 'react';
import AdminSidebarNav from '../components/AdminSidebarNav';
import ViewAllUsersTable from '../components/Table/ViewAllUsersTable';

const ViewAllUsers = () => {
    return (
        <div>
            <AdminSidebarNav />
            <ViewAllUsersTable />
        </div>
    );
};

export default ViewAllUsers;