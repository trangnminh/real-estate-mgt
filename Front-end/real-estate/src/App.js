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
import AdminCalendar from './pages/AdminCalendar';

function App() {
  return (
    <div className="App">
      <NavBar />
      <Routes>
        {/* basic routes */}
        <Route path="/" exact={true} element={<Home />} />
        <Route path="/rental" exact={true} element={<Rental />} />
        <Route path="/help" exact={true} element={<Help />} />
        <Route path="/viewDeatil/:id" exact={true} element={<ViewDetail />} />
        <Route path="/register" exact={true} element={<Register />} />
        <Route path="/myPage" exact={true} element={<MyPage />} />

        {/* logged in users routes */}
        <Route path="/auth/calendar" exact={true} element={<Calendar />} />
        <Route path="/auth/profile" exact={true} element={<Profile />} />

        {/* admin routes */}
        <Route path="/auth/admin/calendar" exact={true} element={<AdminCalendar />} />
        <Route path="/auth/admin/addHouse" exact={true} element={<Calendar />} />
        <Route path="/auth/admin/viewRentalHouses" exact={true} element={<Calendar />} />
        <Route path="/auth/admin/viewUsers" exact={true} element={<Calendar />} />

      </Routes>
    </div>
  );
}

export default App;
