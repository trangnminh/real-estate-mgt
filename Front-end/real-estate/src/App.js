import { Routes, Route } from 'react-router-dom';
import Calendar from './pages/Calendar';
import NavBar from './components/NavBar';
import Profile from './pages/Profile';
import Help from './pages/Help';
import Home from './pages/Home';
import MyPage from './pages/MyPage';
import Register from './pages/Register';
import Rental from './pages/Rental';
import ViewDetail from './pages/ViewDetail';
import BookMeeting from './pages/BookMeeting';
import AdminCalendar from './pages/AdminCalendar';
import AddHouse from './pages/AddHouse';
import ViewRentalHouses from './pages/ViewRentalHouses';
import ViewAllUsers from './pages/ViewAllUsers';

function App() {
  return (
    <div className="App">
      <NavBar />
      <Routes>
        {/* basic routes */}
        <Route path="/" exact={true} element={<Home />} />
        <Route path="/rental" exact={true} element={<Rental />} />
        <Route path="/help" exact={true} element={<Help />} />
        <Route path="/myPage" exact={true} element={<MyPage />} />
        <Route path="/BookMeeting/:id" exact={true} element={<BookMeeting />} />
        <Route path="/viewDeatil/:id" exact={true} element={<ViewDetail />} />
        <Route path="/register" exact={true} element={<Register />} />
        <Route path="/myPage" exact={true} element={<MyPage />} />

        {/* logged in users routes */}
        <Route path="/auth/calendar" exact={true} element={<Calendar />} />
        <Route path="/auth/profile" exact={true} element={<Profile />} />

        {/* admin routes */}
        <Route path="/auth/admin/calendar" exact={true} element={<AdminCalendar />} />
        <Route path="/auth/admin/addHouse" exact={true} element={<AddHouse />} />
        <Route path="/auth/admin/viewRentalHouses" exact={true} element={<ViewRentalHouses />} />
        <Route path="/auth/admin/viewUsers" exact={true} element={<ViewAllUsers />} />

      </Routes>
    </div>
  );
}

export default App;
