import React, { useEffect } from "react";
import "./CustomPopup.css";

const CustomPopup = ({ message, type = "success", onClose, duration = 3000 }) => {
  useEffect(() => {
    if (duration > 0) {
      const timer = setTimeout(() => {
        onClose();
      }, duration);
      
      return () => clearTimeout(timer);
    }
  }, [duration, onClose]);

  const getIcon = () => {
    switch (type) {
      case "success":
        return "";
      case "error":
        return "";
      case "warning":
        return "";
      case "info":
        return "ℹ";
      default:
        return "";
    }
  };

  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  return (
    <div className="popup-overlay" onClick={handleBackdropClick}>
      <div className={`popup-container popup-${type}`}>
        <div className="popup-header">
          <div className="popup-icon">{getIcon()}</div>
          <button className="popup-close" onClick={onClose}>×</button>
        </div>
        <div className="popup-content">
          <h3 className="popup-title">
            {type === "success" ? "Success!" : 
             type === "error" ? "Error!" :
             type === "warning" ? "Warning!" : "Information"}
          </h3>
          <p className="popup-message">{message}</p>
        </div>
        <div className="popup-footer">
          <button className="popup-button" onClick={onClose}>
            OK
          </button>
        </div>
      </div>
    </div>
  );
};

export default CustomPopup;