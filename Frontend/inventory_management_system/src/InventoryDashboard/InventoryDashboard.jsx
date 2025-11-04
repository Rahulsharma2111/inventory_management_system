import React, { useState, useEffect } from "react";
import "./InventoryDashboard.css";
import Navbar from "../Navbar/Navbar";
import { getDashboardStatsApi } from "../Service/ApiService";
import { useNavigate, useParams } from "react-router-dom";
import FileUpload from "../FileUpload/FileUpload";
import FileUploadWithjson from "../FileWithJSON/FileUploadWithjson";
import FileManager from "../FileManager/FileManager";

const InventoryDashboard = () => {
  const navigate = useNavigate();

  // Initialize with sample data
  useEffect(() => {
    const jwtToken = localStorage.getItem("token");
    if (!jwtToken || jwtToken == null) {
      navigate("/");
    }
  }, []);

  const getCurrentMonth = () => {
    const now = new Date();
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(
      2,
      "0"
    )}`;
  };

  const [dashboardStats, setDashboardStats] = useState({
    totalProducts: 0,
    totalOrders: 0,
    totalSalesAmount: 0,
  });

  const organisationId = localStorage.getItem("id");

  useEffect(() => {
    const fetchDashboardStats = async () => {
      try {
        const data = await getDashboardStatsApi(organisationId);
        console.log(data);
        setDashboardStats(data);
      } catch (err) {
        console.error("Failed to fetch dashboard stats:", err);
        setError("Unable to load dashboard data");
      }
    };

    if (organisationId) {
      fetchDashboardStats();
    }
  }, [organisationId]);

  return (
    <div className="inventory-dashboard">
      <Navbar></Navbar>

      {/* Header */}
      <div className="dashboard-header">
        <h1>Inventory Dashboard</h1>
      </div>

      {/* Summary Cards */}
      <div className="summary-cards">
        <div className="summary-card">
          <div className="card-icon">📦</div>
          <div className="card-content">
            <h3>Total Products</h3>
            <span className="card-value">{dashboardStats.totalProducts}</span>
          </div>
        </div>
        <div className="summary-card">
          <div className="card-icon">📊</div>
          <div className="card-content">
            <h3>Total Orders</h3>
            <span className="card-value">{dashboardStats.totalOrders}</span>
          </div>
        </div>
        <div className="summary-card">
          <div className="card-icon">💰</div>
          <div className="card-content">
            <h3>Total Value</h3>
            <span className="card-value">
              ${dashboardStats.totalSalesAmount.toFixed(2)}
            </span>
          </div>
        </div>
      </div>
      <br></br>
      <br></br>
      <br></br>
      <FileManager></FileManager>

    </div>
  );
};

export default InventoryDashboard;
