import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";
import HomePage from "./HomePage/HomePage";
import InventoryTable from "./Inventory/InventoryTable";
import SellDetails from "./SellDetails/SellDetails";
import InventoryDashboard from "./InventoryDashboard/InventoryDashboard";
import LogoutPopup from "./LogOut/Logout";
import { ToastContainer } from "react-toastify";
import FileUpload from "./FileUpload/FileUpload";

function App() {
  //  const organisationId=localStorage.getItem("id");
  return (
    <>
      <Router>
        <div className="App">
          {/* <Navbar /> */}
          <main className="main-content">
            <Routes>
              <Route path="" element={<HomePage />} />
              <Route path="/inventory" element={<InventoryTable />} />
              <Route path="/dashboard" element={<InventoryDashboard />} />
              <Route path="/sell-record/:organisationId" element={<SellDetails />} />
              <Route path={"/file_upload"} element={<FileUpload />} />
              {/* <Route path="/login-out" element={<LogoutPopup />} /> */}
              <Route path="*" element={<NotFound />} />
            </Routes>
          </main>
        </div>
      </Router>

          <ToastContainer
      position="top-right"
      autoClose={3000}
      hideProgressBar={false}
      newestOnTop
      closeOnClick
      rtl={false}
      pauseOnFocusLoss
      draggable
      pauseOnHover
      theme="light"
    />
    </>
  );
}

export default App;

const NotFound = () => {
  return (
    <div className="not-found">
      <h2>404 - Page Not Found</h2>
      <p>The page you're looking for doesn't exist.</p>
    </div>
  );
};
