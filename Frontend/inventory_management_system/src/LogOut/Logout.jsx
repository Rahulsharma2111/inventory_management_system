import React from "react";
import "./Logout.css";

const Logout = ({ show, onConfirm, onCancel }) => {
  if (!show) return null; 

  return (
    <div className="logout-overlay">
      <div className="logout-popup">
        <h3>Confirm Logout</h3>
        <p>Are you sure you want to log out?</p>
        <div className="popup-buttons">
          <button className="btn confirm" onClick={onConfirm}>
            Yes, Logout
          </button>
          <button className="btn cancel" onClick={onCancel}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

export default Logout;
