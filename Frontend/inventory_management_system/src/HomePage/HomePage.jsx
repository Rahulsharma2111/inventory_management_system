import React, { useState } from "react";
import "./HomePage.css";
import { loginApi, registerOrganizationApi } from "../Service/ApiService";
import CustomPopup from "../Components/CustomPopup";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const HomePage = () => {
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
    const [loginData, setLoginData] = useState({
    email: "",
    password: ""
  });
  const [formData, setFormData] = useState({
    name: "",
    registrationNumber: "",
    type: "",
    email: "",
    phone: "",
    website: "",
    gstNumber: "",
    address: "",
    city: "",
    state: "",
    country: "",
    pincode: "",
    password: "",
  });
  const [errors, setErrors] = useState({});
  const [isLoginLoading, setIsLoginLoading] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [popupState, setPopupState] = useState({
    show: false,
    message: "",
    type: "success",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: "",
      }));
    }
  };
  const showPopup = (message, type = "success") => {
    setPopupState({
      show: true,
      message,
      type,
    });
  };

  const hidePopup = () => {
    setPopupState({
      show: false,
      message: "",
      type: "success",
    });
  };

  const validateForm = () => {
    const newErrors = {};

    Object.keys(formData).forEach((key) => {
      if (!formData[key].trim()) {
        newErrors[key] = "This field is required";
      }
    });

    if (formData.email && !/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Please enter a valid email address";
    }

    if (formData.phone && !/^\d{10}$/.test(formData.phone)) {
      newErrors.phone = "Please enter a valid 10-digit phone number";
    }

    if (formData.password && formData.password.length < 6) {
      newErrors.password = "Password must be at least 6 characters long";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // const handleSubmit = async (e) => {
  //   e.preventDefault();

  //   if (!validateForm()) {
  //     return;
  //   }

  //   setIsLoading(true);

  //   try {
  //     const result = await registerOrganizationApi(formData);

  //     console.log("API Response:", result);
  //     showPopup("Organization registered successfully!", "success");

  //     // if (apiMessage.popUp == true) {
  //     //   CustomPopup(apiMessage.message);
  //     // }

  //     setTimeout(() => {
  //       setShowModal(false);
  //       setFormData({
  //         name: "",
  //         registrationNumber: "",
  //         type: "",
  //         email: "",
  //         phone: "",
  //         website: "",
  //         gstNumber: "",
  //         address: "",
  //         city: "",
  //         state: "",
  //         country: "",
  //         pincode: "",
  //         password: "",
  //       });
  //     }, 2000);
  //   } catch (error) {
  //     console.error("Registration failed:", error);
  //     showPopup(
  //       error.message || "Registration failed. Please try again.",
  //       "error"
  //     );
  //     // if (apiMessage.popUp == true) {
  //     //   CustomPopup(apiMessage.message);
  //     // }
  //   } finally {
  //     setIsLoading(false);
  //   }
  // };

  const ShowLoginFrom=()=>{
   setShowLoginModal(true);
}

  const handleSubmit = async (e) => {
  e.preventDefault();

  if (!validateForm()) {
    return;
  }

  setIsLoading(true);

   const response = await registerOrganizationApi(formData);
  try {
    
    console.log("Sending data:", formData);
    
    const result=response;
    toast.success("Register successfully!");
    console.log("API Response:", result);
   
    //  setShowModal(false);
            setFormData({
          name: "",
          registrationNumber: "",
          type: "",
          email: "",
          phone: "",
          website: "",
          gstNumber: "",
          address: "",
          city: "",
          state: "",
          country: "",
          pincode: "",
          password: "",
        });
    setShowLoginModal(true);
  } catch (error) {
    console.error("Registration failed:", error);
    console.error("Request data that failed:", formData); 
  } finally {
    setIsLoading(false);
  }
};
  const handleCloseModal = () => {
    setShowModal(false);
    setErrors({});
  };

    const handleCloseLoginModal = () => {
    setShowLoginModal(false);
    setLoginData({ email: "", password: "" });
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();

    setIsLoginLoading(true);

    try {
      // Here you would call your login API
      const result = await loginApi(loginData);
      console.log("Login data:", result);
      localStorage.setItem("token",result.jwtToken);
      localStorage.setItem("id",result.id);
      localStorage.setItem("organigastionName",result.name);
      toast.success("Login successfully")
      navigate('/dashboard')
      setTimeout(() => {
        setShowLoginModal(false);
        setLoginData({ email: "", password: "" });
      }, 2000);
    } catch (error) {
      console.error("Login failed:", error);
      // toast.error("Login failed. Please try again.");
    } finally {
      setIsLoginLoading(false);
    }
  };

   const handleLoginInputChange = (e) => {
    const { name, value } = e.target;
    setLoginData(prev => ({
      ...prev,
      [name]: value
    }));
   
  };

  return (
    <>
      {popupState.show && (
        <CustomPopup
          message={popupState.message}
          type={popupState.type}
          onClose={hidePopup}
        />
      )}

      {/* Hero Section */}
      <div className="hero-section">
        <div className="container">
          <div className="hero-content">
            <div className="hero-text">
              <h1 className="hero-title">Welcome to Our Platform</h1>
              <p className="hero-description">
                Streamline your business operations with our comprehensive
                solution. Get started by registering your organization today.
              </p>
              <button className="cta-button" onClick={() => setShowModal(true)}>
                Register Now
              </button>

              <button
                className="cta-button"
                style={{"marginLeft": "104px"}}
                onClick={() => setShowLoginModal(true)}
              >
                Login Now
              </button>
            </div>
            <div className="hero-side">
              <div className="quick-start-card">
                <h3>Quick Start</h3>
                <p>Complete your registration in just a few minutes</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="features-section">
        <div className="container">
          <div className="section-header">
            <h2 className="section-title">Why Choose Us?</h2>
            <p className="section-subtitle">
              Discover the benefits of our platform
            </p>
          </div>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon bg-primary">⚡</div>
              <h5 className="feature-title">Fast Registration</h5>
              <p className="feature-description">
                Quick and easy registration process to get you started in
                minutes.
              </p>
            </div>
            <div className="feature-card">
              <div className="feature-icon bg-success">🛡️</div>
              <h5 className="feature-title">Secure & Reliable</h5>
              <p className="feature-description">
                Your data is protected with enterprise-grade security measures.
              </p>
            </div>
            <div className="feature-card">
              <div className="feature-icon bg-warning">📊</div>
              <h5 className="feature-title">Comprehensive Dashboard</h5>
              <p className="feature-description">
                Access all your business data and analytics in one place.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Registration Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Organization Registration</h5>
                <button
                  type="button"
                  className="close-button"
                  onClick={handleCloseModal}
                >
                  ×
                </button>
              </div>
              <form onSubmit={handleSubmit}>
                <div className="modal-body">
                  <div className="form-container">
                    {/* Basic Information */}
                    <div className="form-section">
                      <div className="section-header-inline">
                        <h6 className="section-title-small">
                          Basic Information
                        </h6>
                      </div>
                      <div className="form-grid">
                        <div className="form-group">
                          <label htmlFor="name" className="form-label">
                            Organization Name *
                          </label>
                          <input
                            type="text"
                            className={`form-input ${
                              errors.name ? "invalid" : ""
                            }`}
                            id="name"
                            name="name"
                            value={formData.name}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.name && (
                            <div className="error-message">{errors.name}</div>
                          )}
                        </div>
                        <div className="form-group">
                          <label
                            htmlFor="registrationNumber"
                            className="form-label"
                          >
                            Registration Number *
                          </label>
                          <input
                            type="text"
                            className={`form-input ${
                              errors.registrationNumber ? "invalid" : ""
                            }`}
                            id="registrationNumber"
                            name="registrationNumber"
                            value={formData.registrationNumber}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.registrationNumber && (
                            <div className="error-message">
                              {errors.registrationNumber}
                            </div>
                          )}
                        </div>
                        <div className="form-group full-width">
                          <label htmlFor="type" className="form-label">
                            Organization Type *
                          </label>
                          <input
                            type="text"
                            className={`form-input ${
                              errors.type ? "invalid" : ""
                            }`}
                            id="type"
                            name="type"
                            value={formData.type}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.type && (
                            <div className="error-message">{errors.type}</div>
                          )}
                        </div>
                      </div>
                    </div>

                    {/* Contact Information */}
                    <div className="form-section">
                      <div className="section-header-inline">
                        <h6 className="section-title-small">
                          Contact Information
                        </h6>
                      </div>
                      <div className="form-grid">
                        <div className="form-group">
                          <label htmlFor="email" className="form-label">
                            Email Address *
                          </label>
                          <input
                            type="email"
                            className={`form-input ${
                              errors.email ? "invalid" : ""
                            }`}
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.email && (
                            <div className="error-message">{errors.email}</div>
                          )}
                        </div>
                        <div className="form-group">
                          <label htmlFor="phone" className="form-label">
                            Phone Number *
                          </label>
                          <input
                            type="tel"
                            className={`form-input ${
                              errors.phone ? "invalid" : ""
                            }`}
                            id="phone"
                            name="phone"
                            value={formData.phone}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.phone && (
                            <div className="error-message">{errors.phone}</div>
                          )}
                        </div>
                        <div className="form-group">
                          <label htmlFor="website" className="form-label">
                            Website *
                          </label>
                          <input
                            type="url"
                            className={`form-input ${
                              errors.website ? "invalid" : ""
                            }`}
                            id="website"
                            name="website"
                            value={formData.website}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.website && (
                            <div className="error-message">
                              {errors.website}
                            </div>
                          )}
                        </div>
                        <div className="form-group">
                          <label htmlFor="gstNumber" className="form-label">
                            GST Number *
                          </label>
                          <input
                            type="text"
                            className={`form-input ${
                              errors.gstNumber ? "invalid" : ""
                            }`}
                            id="gstNumber"
                            name="gstNumber"
                            value={formData.gstNumber}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.gstNumber && (
                            <div className="error-message">
                              {errors.gstNumber}
                            </div>
                          )}
                        </div>
                      </div>
                    </div>

                    {/* Address Information */}
                    <div className="form-section">
                      <div className="section-header-inline">
                        <h6 className="section-title-small">
                          Address Information
                        </h6>
                      </div>
                      <div className="form-grid">
                        <div className="form-group full-width">
                          <label htmlFor="address" className="form-label">
                            Address *
                          </label>
                          <textarea
                            className={`form-input ${
                              errors.address ? "invalid" : ""
                            }`}
                            id="address"
                            name="address"
                            rows="3"
                            value={formData.address}
                            onChange={handleInputChange}
                            required
                          ></textarea>
                          {errors.address && (
                            <div className="error-message">
                              {errors.address}
                            </div>
                          )}
                        </div>
                        <div className="form-group">
                          <label htmlFor="city" className="form-label">
                            City *
                          </label>
                          <input
                            type="text"
                            className={`form-input ${
                              errors.city ? "invalid" : ""
                            }`}
                            id="city"
                            name="city"
                            value={formData.city}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.city && (
                            <div className="error-message">{errors.city}</div>
                          )}
                        </div>
                        <div className="form-group">
                          <label htmlFor="state" className="form-label">
                            State *
                          </label>
                          <input
                            type="text"
                            className={`form-input ${
                              errors.state ? "invalid" : ""
                            }`}
                            id="state"
                            name="state"
                            value={formData.state}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.state && (
                            <div className="error-message">{errors.state}</div>
                          )}
                        </div>
                        <div className="form-group">
                          <label htmlFor="country" className="form-label">
                            Country *
                          </label>
                          <input
                            type="text"
                            className={`form-input ${
                              errors.country ? "invalid" : ""
                            }`}
                            id="country"
                            name="country"
                            value={formData.country}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.country && (
                            <div className="error-message">
                              {errors.country}
                            </div>
                          )}
                        </div>
                        <div className="form-group">
                          <label htmlFor="pincode" className="form-label">
                            Pincode *
                          </label>
                          <input
                            type="text"
                            className={`form-input ${
                              errors.pincode ? "invalid" : ""
                            }`}
                            id="pincode"
                            name="pincode"
                            value={formData.pincode}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.pincode && (
                            <div className="error-message">
                              {errors.pincode}
                            </div>
                          )}
                        </div>
                      </div>
                    </div>

                    {/* Security */}
                    <div className="form-section">
                      <div className="section-header-inline">
                        <h6 className="section-title-small">Security</h6>
                      </div>
                      <div className="form-grid">
                        <div className="form-group full-width">
                          <label htmlFor="password" className="form-label">
                            Password *
                          </label>
                          <input
                            type="password"
                            className={`form-input ${
                              errors.password ? "invalid" : ""
                            }`}
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleInputChange}
                            required
                          />
                          {errors.password && (
                            <div className="error-message">
                              {errors.password}
                            </div>
                          )}
                          <div className="form-help">
                            Password must be at least 6 characters long.
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="modal-footer">
                    <button
                      type="button"
                      className="btn-secondary"
                      onClick={handleCloseModal}
                    >
                      Cancel
                    </button>
                    <button type="submit" className="btn-primary">
                      Register Organization
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

       {/* Login Modal */}
      {showLoginModal && (
        <div className="modal-overlay">
          <div className="modal-dialog modal-sm" style={{"width": "400px"}}>
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Login to Your Account</h5>
                <button
                  type="button"
                  className="close-button"
                  onClick={handleCloseLoginModal}
                  disabled={isLoginLoading}
                >
                  ×
                </button>
              </div>
              <form onSubmit={handleLoginSubmit}>
                <div className="modal-body">
                  <div className="form-container">
                    <div className="form-section">
                      <div className="form-grid">
                        <div className="form-group full-width">
                          <label htmlFor="loginEmail" className="form-label">
                            Email Address *
                          </label>
                          <input
                            type="email"
                            className={`form-input `}
                            id="loginEmail"
                            name="email"
                            value={loginData.email}
                            onChange={handleLoginInputChange}
                            required
                            disabled={isLoginLoading}
                            placeholder="Enter your email"
                          />
                         
                        </div>
                        <div className="form-group full-width">
                          <label htmlFor="loginPassword" className="form-label">
                            Password *
                          </label>
                          <input
                            type="password"
                            className={`form-input `}
                            id="loginPassword"
                            name="password"
                            value={loginData.password}
                            onChange={handleLoginInputChange}
                            required
                            disabled={isLoginLoading}
                            placeholder="Enter your password"
                          />
                        
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="modal-footer">
                  <button
                    type="button"
                    className="btn-secondary"
                    onClick={handleCloseLoginModal}
                    disabled={isLoginLoading}
                  >
                    Cancel
                  </button>
                  <button 
                    type="submit" 
                    className="btn-primary"
                    disabled={isLoginLoading}
                  >
                    {isLoginLoading ? "Logging in..." : "Login"}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

    </>
  );
};

export default HomePage;
