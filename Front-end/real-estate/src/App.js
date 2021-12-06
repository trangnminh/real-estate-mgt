import { Routes, Route } from 'react-router-dom';
import NavBar from './components/NavBar';
import Help from './pages/Help';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Rental from './pages/Rental';
import ViewDetail from './pages/ViewDetail';

function App() {
  return (
    <div className="App">
      <NavBar />
      <Routes>
        <Route path="/" exact={true} element={<Home />} />
        <Route path="/rental" exact={true} element={<Rental />} />
        <Route path="/help" exact={true} element={<Help />} />
        <Route path="/login" exact={true} element={<Login />} />
        <Route path="/viewDeatil/:id" exact={true} element={<ViewDetail />} />
        <Route path="/auth/register" exact={true} element={<Register />} />
      </Routes>
    </div>
  );
}

export default App;
