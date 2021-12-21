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
import AddHouse from './pages/AddHouse';


function App() {
  return (
    <div className="App">
      <NavBar />
      <Routes>
        <Route path="/" exact={true} element={<Home />} />
        <Route path="/rental" exact={true} element={<Rental />} />
        <Route path="/help" exact={true} element={<Help />} />
        <Route path="/myPage" exact={true} element={<MyPage />} />
        <Route path="/detail/:id" exact={true} element={<ViewDetail />} />
        <Route path="/auth/register" exact={true} element={<Register />} />
        <Route path="/auth/calendar" exact={true} element={<Calendar />} />
        <Route path="/auth/profile" exact={true} element={<Profile />} />
        <Route path="/admin/add" exact={true} element={<AddHouse />} />
      </Routes>
    </div>
  );
}

export default App;
