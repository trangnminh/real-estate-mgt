import React, { useEffect, useMemo, useState } from 'react';
import { Container, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { SelectColumnFilter } from './Filter';
import TableContainer from './TableContainer';
import axios from 'axios'

const ViewAllUsersTable = () => {

    const [data, setData] = useState([]);

    const fetchAllUsers = async () => {
        try {
            const response = await axios.get(
                'http://localhost:8080/api/v1/users'
            );
            setData(response.data);
        } catch (e) {
        }
    };

    useEffect(() => {
        fetchAllUsers();
    }, []);

    const columns = useMemo(
        () => [
            {
                Header: '#',
                accessor: 'userId',
                disableSortBy: true,
                Filter: SelectColumnFilter,
                filter: 'equals',
            },
            {
                Header: 'Name',
                accessor: 'fullName',
            },
            {
                Header: 'Email',
                accessor: 'email',
            },
            {
                Header: 'Phone Number',
                accessor: 'phoneNumber',
            },
            {
                Header: 'Day of Birthday',
                accessor: 'dob',
            },
        ],
        []
    );
    return (
        <Container style={{ marginTop: 50 }}>
            <TableContainer
                columns={columns}
                data={data}
            />
        </Container>
    );
};

export default ViewAllUsersTable;