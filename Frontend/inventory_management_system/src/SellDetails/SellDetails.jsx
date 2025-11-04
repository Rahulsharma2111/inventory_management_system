import React, { useEffect, useState } from "react";
import "./SellDetails.css";
import Navbar from "../Navbar/Navbar";
import {
  createOrderApi,
  getAllOrderByOrganisationIdApi,
  getAllProductApi,
} from "../Service/ApiService";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";

const SellDetails = () => {
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // const sellOrders = [
  //   {
  //     id: 1,
  //     name: "John Doe",
  //     email: "john@example.com",
  //     phone: "+1234567890",
  //     price: 299.99,
  //     quantity: 2,
  //     productId: 101,
  //     createdAt: new Date('2024-01-15'),
  //     productName: "Wireless Headphones"
  //   },
  //   {
  //     id: 2,
  //     name: "Jane Smith",
  //     email: "jane@example.com",
  //     phone: "+0987654321",
  //     price: 599.99,
  //     quantity: 1,
  //     productId: 102,
  //     createdAt: new Date('2024-01-16'),
  //     productName: "Smartphone"
  //   }
  // ];

  const [sellOrders, setSellOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const { organisationId } = useParams();
  useEffect(() => {
    const jwtToken = localStorage.getItem("token");
    if (!jwtToken || jwtToken == null) {
      navigate("/");
    }
    fetchOrders();
  }, []);

  const [products, setProducts] = useState([]);
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getAllProductApi(organisationId);
        setProducts(data);
      } catch (error) {
        console.log(error);
        toast.error("Failed to fetch products");
      }
    };
    fetchProducts();
  }, [organisationId]);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      setError(null);
      const orders = await getAllOrderByOrganisationIdApi(organisationId);
      setSellOrders(orders);
    } catch (err) {
      console.error("Failed to fetch orders:", err);
      setError("Failed to load orders. Please try again.");
      setSellOrders([]);
    } finally {
      setLoading(false);
    }
  };

  const handleRefresh = () => {
    fetchOrders();
  };

  const handleOrderClick = (order) => {
    setSelectedOrder(order);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedOrder(null);
  };

  const formatDate = (date) => {
    return new Date(date).toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    price: "",
    quantity: "",
    productId: "",
    organisationId: "",
    totalAmount: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    setFormData((prevState) => {
      const updatedData = {
        ...prevState,
        [name]: value,
      };

      if (name === "price" || name === "quantity") {
        const price = parseFloat(updatedData.price) || 0;
        const quantity = parseInt(updatedData.quantity) || 0;
        updatedData.totalAmount = price * quantity;
      }

      return updatedData;
    });
  };

  const handleCreateOrderSubmit = async (e) => {
    e.preventDefault();
    try {
      const orgId = localStorage.getItem("id");

      const dataWithOrgId = {
        ...formData,
        organisationId: orgId,
      };

      const result = await createOrderApi(dataWithOrgId);

      console.log("Order data:", dataWithOrgId);
      console.log("Order Created:", result);

      setIsPopupOpen(false);
      toast.success("Order created successfully");
      setFormData({
        name: "",
        email: "",
        phone: "",
        price: "",
        quantity: "",
        productId: "",
        organisationId: "",
        totalAmount: "",
      });
      handleRefresh();
    } catch (error) {
      console.error("Registration failed:", error);
      console.error("Request data that failed:", formData);
      toast.error(error);
      // toast.error("order failed to create. Please try again.")
    }
  };

  const openPopup = () => {
    setIsPopupOpen(true);
  };

  const closePopup = () => {
    setIsPopupOpen(false);
    // Reset form when closing
    setFormData({
      name: "",
      email: "",
      phone: "",
      price: "",
      quantity: "",
      productId: "",
      organisationId: "",
    });
  };

  const handleProductSelect = (e) => {
    const selectedId = e.target.value;
    const selectedProduct = products.find((p) => p.id === parseInt(selectedId));

    console.log("Selected product:", selectedProduct);

    setFormData({
      ...formData,
      productId: selectedId,
      price: selectedProduct.price,
      quantity: selectedProduct.stockQuantity,
      totalAmount: selectedProduct.price * selectedProduct.stockQuantity,
    });
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading orders...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-container">
        <p className="error-message">{error}</p>
        <button onClick={handleRefresh} className="retry-btn">
          Try Again
        </button>
      </div>
    );
  }

  return (
    <div className="sell-details-container">
      <Navbar></Navbar>
      <div>
        <h1 className="page-title">Sell Orders</h1>

        <div className="create-order-container">
          <button className="create-order-btn" onClick={openPopup}>
            Create Order
          </button>

          {isPopupOpen && (
            <div className="popup-overlay" onClick={closePopup}>
              <div
                className="popup-content"
                onClick={(e) => e.stopPropagation()}
              >
                <div className="popup-header">
                  <h2>Create New Order</h2>
                  <button className="close-btn" onClick={closePopup}>
                    ×
                  </button>
                </div>

                <form className="order-form" onSubmit={handleCreateOrderSubmit}>
                  <div className="form-group">
                    <label htmlFor="name">Name *</label>
                    <input
                      type="text"
                      id="name"
                      name="name"
                      value={formData.name}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="email">Email *</label>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="phone">Phone *</label>
                    <input
                      type="tel"
                      id="phone"
                      name="phone"
                      value={formData.phone}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                  {/* ------------------------------------------- */}

                  <div className="form-group">
                    <label htmlFor="productId">Product *</label>
                    <select
                      type="number"
                      id="productId"
                      name="productId"
                      min="1"
                      value={formData.productId}
                      onChange={handleInputChange}
                      onClick={handleProductSelect}
                      required
                    >
                      <option value="">Select product</option>
                      {products.map((product) => (
                        <option key={product.id} value={product.id}>
                          {product.name}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="price">Price per unit*</label>
                    <input
                      type="number"
                      id="price"
                      name="price"
                      step="0.01"
                      min="0"
                      value={formData.price}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="quantity">Quantity *</label>
                    <input
                      type="number"
                      id="quantity"
                      name="quantity"
                      min="1"
                      value={formData.quantity}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="totalAmount">Total Amount</label>
                    <input
                      type="number"
                      id="totalAmount"
                      name="totalAmount"
                      value={formData.totalAmount}
                      onChange={handleInputChange}
                      readOnly
                    />
                  </div>

                  <div className="form-actions">
                    <button
                      type="button"
                      className="cancel-btn"
                      onClick={closePopup}
                    >
                      Cancel
                    </button>
                    <button type="submit" className="submit-btn">
                      Create Order
                    </button>
                  </div>
                </form>
              </div>
            </div>
          )}
        </div>
      </div>
      <div className="orders-grid">
        {sellOrders.map((order) => (
          <div
            key={order.id}
            className="order-card"
            onClick={() => handleOrderClick(order)}
          >
            <div className="order-header">
              <h3 className="product-name">{order.productName}</h3>
              <span className="order-id">#{order.id}</span>
            </div>

            <div className="order-preview">
              <div className="preview-item">
                <span className="label">Customer:</span>
                <span className="value">{order.name}</span>
              </div>
              <div className="preview-item">
                <span className="label">Price per unit:</span>
                <span className="value price">${order.price}</span>
              </div>
              <div className="preview-item">
                <span className="label">Quantity:</span>
                <span className="value">{order.quantity}</span>
              </div>
              <div className="preview-item">
                <span className="label">Total Amount:</span>
                <span className="value">{order.totalAmount}</span>
              </div>
              <div className="preview-item">
                <span className="label">Date:</span>
                <span className="value">{formatDate(order.createdAt)}</span>
              </div>
            </div>

            <div className="view-details">Click to view details →</div>
          </div>
        ))}
      </div>

      {/* Modal Popup */}
      {isModalOpen && selectedOrder && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Order Details</h2>
              <button className="close-button" onClick={closeModal}>
                ×
              </button>
            </div>

            <div className="modal-body">
              <div className="details-section">
                <div>
                  <h3>Order Summary</h3>
                  <div className="detail-row">
                    <span className="detail-label">Customer Email:</span>
                    <span className="detail-value">{selectedOrder.email}</span>
                  </div>
                  <div className="detail-row">
                    <span className="detail-label">Total Price:</span>
                    <span className="detail-value price">
                      ${selectedOrder.totalAmount}
                    </span>
                  </div>
                </div>
                <h3>Order Information</h3>
                <div className="detail-row">
                  <span className="detail-label">Order ID:</span>
                  <span className="detail-value">#{selectedOrder.id}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Product:</span>
                  <span className="detail-value">
                    {selectedOrder.productName}
                  </span>
                </div>
                {/* <div className="detail-row">
                  <span className="detail-label">Product ID:</span>
                  <span className="detail-value">{selectedOrder.productId}</span>
                </div> */}
                <div className="detail-row">
                  <span className="detail-label">Quantity:</span>
                  <span className="detail-value">{selectedOrder.quantity}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Unit Price:</span>
                  <span className="detail-value">
                    ${selectedOrder.price.toFixed(2)}
                  </span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Total Price:</span>
                  <span className="detail-value price">
                    ${selectedOrder.totalAmount}
                  </span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Order Date:</span>
                  <span className="detail-value">
                    {formatDate(selectedOrder.createdAt)}
                  </span>
                </div>
              </div>

              <div className="details-section">
                <h3>Customer Information</h3>
                <div className="detail-row">
                  <span className="detail-label">Name:</span>
                  <span className="detail-value">{selectedOrder.name}</span>
                </div>
                {/* <div className="detail-row">
                  <span className="detail-label">Email:</span>
                  <span className="detail-value">{selectedOrder.email}</span>
                </div> */}
                <div className="detail-row">
                  <span className="detail-label">Phone:</span>
                  <span className="detail-value">{selectedOrder.phone}</span>
                </div>
              </div>
            </div>

            <div className="modal-footer">
              <button className="btn-primary" onClick={closeModal}>
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default SellDetails;
