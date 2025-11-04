import React, { useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import "./Navbar.css";
import Logout from "../LogOut/Logout";
import { toast } from "react-toastify";
import {
  deleteLogoApi,
  getLogoImageApi,
  uploadImageApi,
} from "../Service/ApiService";
import { FaTrash } from "react-icons/fa";

const Navbar = () => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path;
  };

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  const closeMobileMenu = () => {
    setIsMobileMenuOpen(false);
  };

  const organisationId = localStorage.getItem("id");

  const [showLogoutPopup, setShowLogoutPopup] = useState(false);
  const navigate = useNavigate();

  const handleConfirmLogout = () => {
    localStorage.clear();
    setShowLogoutPopup(false);
    navigate("/");
    toast.success("Logout successfully");
  };

  const [imagePath, setImagePath] = useState(null);
  const fileInputRef = useRef(null);
  const fetchLogo = async () => {
    try {
      const logoImage = await getLogoImageApi();
      setImagePath(logoImage);
    } catch (error) {
      console.error("Error fetching logo image:", error);
    }
  };
  useEffect(() => {
    fetchLogo();
  }, []);

  const handleLogoClick = () => {
    fileInputRef.current.click();
  };

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    console.log("image Path ", file);
    try {
      const result = await uploadImageApi(file);
      setImagePath(result);
      // localStorage.setItem("logoPath", result);
      // console.log(result);
      fetchLogo();
      toast.success("Logo uploaded successfully");
    } catch (error) {
      toast.error("Failed to upload logo");
    }
  };

  const onDelete = async () => {
    if (!window.confirm("Are you sure you want to delete this product?"))
      return;
    try {
      const deleteRespose = await deleteLogoApi();
      // console.log("Product deleted:", result);
      setImagePath("");
      toast.success("Logo deleted successfully!");
    } catch (error) {
      toast.error("Fail to Delete");
      // alert("Failed to delete product: " + error.message);
    }
  };

  return (
    <>
      <nav className="navbar">
        <div className="nav-container">
          {/* Logo/Brand */}
          <div className="nav-brand">
            {/* <Link to="/" onClick={closeMobileMenu}></Link> */}
            <div>
              <img
                src={imagePath}
                // src="https://www.shutterstock.com/image-vector/vector-icon-demo-600nw-1148418773.jpg"
                alt="logo"
                className="nav-logo"
                onClick={handleLogoClick}
              />
              <FaTrash
                style={{ cursor: "pointer", color: "red" }}
                onClick={onDelete}
                title="Delete file"
              />
              {/* <span>Delete</span> */}
            </div>
            <input
              type="file"
              accept="image/*"
              ref={fileInputRef}
              style={{ display: "none" }}
              onChange={handleFileChange}
            />
            <h2>{localStorage.getItem("organigastionName")}</h2>
          </div>

          {/* Mobile Menu Button */}
          <button className="mobile-menu-btn" onClick={toggleMobileMenu}>
            <span></span>
            <span></span>
            <span></span>
          </button>

          {/* Navigation Links */}
          <div
            className={`nav-links ${
              isMobileMenuOpen ? "nav-links-active" : ""
            }`}
          >
            <Link
              to="/dashboard"
              className={`nav-link ${isActive("/dashboard") ? "active" : ""}`}
              onClick={closeMobileMenu}
            >
              Dashboard
            </Link>
            <Link
              to="/inventory"
              className={`nav-link ${isActive("/inventory") ? "active" : ""}`}
              onClick={closeMobileMenu}
            >
              Inventory
            </Link>

            <Link
              to={`/sell-record/${organisationId}`}
              className={`nav-link ${
                isActive(`/sell-record/${organisationId}`) ? "active" : ""
              }`}
              onClick={closeMobileMenu}
            >
              Sell
            </Link>

            <Link
              to="/file_upload"
              className={`nav-link ${isActive("/file_upload") ? "active" : ""}`}
              onClick={closeMobileMenu}
            >
              File Upload
            </Link>

            <button
              className="nav-link logout-btn"
              onClick={() => setShowLogoutPopup(true)}
            >
              Log out
            </button>
          </div>
        </div>
      </nav>
      <Logout
        show={showLogoutPopup}
        onConfirm={handleConfirmLogout}
        onCancel={() => setShowLogoutPopup(false)}
      />
    </>
  );
};

export default Navbar;
