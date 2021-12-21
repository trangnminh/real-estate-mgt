
import Pagination from 'react-bootstrap/Pagination'

const PaginationBar = () => { 
    const active = 2;
    let houses = [];
    for (let number = 1; number <= 10; number++) {
        houses.push(
            <Pagination.Item key={number} active={number === active}>
            {number}
            </Pagination.Item>,
        );
    }

    return (
    <Pagination>
        <Pagination.First />
        <Pagination.Prev />
        {/* <Pagination.Item>{1}</Pagination.Item> */}
        {/* <Pagination.Ellipsis /> */}
        {houses}    
        {/* <Pagination.Ellipsis /> */}
        {/* <Pagination.Item>{20}</Pagination.Item> */}
        <Pagination.Next />
        <Pagination.Last />
    </Pagination>
    );
}

export default PaginationBar