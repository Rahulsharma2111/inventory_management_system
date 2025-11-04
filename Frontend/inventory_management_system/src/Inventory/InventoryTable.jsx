import React, { useEffect, useState } from "react";
import "./InventoryTable.css";
import { addNewProduct, deleteProductApi, getAllProductApi, uppdateProductApi } from "../Service/ApiService";
import Navbar from "../Navbar/Navbar";
import { toast, ToastContainer } from "react-toastify";

const InventoryTable = () => {
 const organisationId=localStorage.getItem("id");

  const [products, setProducts] = useState([
  ]);

 
  useEffect(() => {
        const jwtToken=localStorage.getItem("token") ;
        if(!jwtToken || jwtToken==null){
        navigate('/')
        }
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
const productdata= await getAllProductApi(organisationId);
      setProducts(productdata);
    } catch (err) {
      console.error("Error fetching products:", err);
    } 
  };

  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState({});
  const [showAddForm, setShowAddForm] = useState(false);
  const [newProduct, setNewProduct] = useState({
    name: "",
    category: "",
    description: "",
    price: "",
    stockQuantity: "",
    reorderLevel: ""
  });

  // Start editing a product
  const handleEdit = (product) => {
    setEditingId(product.id);
    setEditForm({ ...product });
  };

  // Save edited product
const handleSave = async (id) => {
  try {
    const response = await uppdateProductApi(id, editForm);
    console.log("API Response:", response);

    const updatedProduct = response.data;

    setProducts(products.map(product =>
      product.id === id ? updatedProduct : product
    ));

    setEditingId(null);
    setEditForm({});
    toast.success("Product Updated Successfully");
  } catch (error) {
    console.log("Failed to update product:", error.message);
    // toast.error(error.message);
    // alert("Error updating product. Please try again.");
  }
};

  // Cancel editing
  const handleCancel = () => {
    setEditingId(null);
    setEditForm({});
  };

  // Handle edit form changes
  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setEditForm(prev => ({
      ...prev,
      [name]: name === 'price' ? parseFloat(value) || 0 : 
              name === 'stockQuantity' || name === 'reorderLevel' ? parseInt(value) || 0 : value
    }));
  };

  // Handle new product form changes
  const handleNewProductChange = (e) => {
    const { name, value } = e.target;
    setNewProduct(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Add new product
  const handleAddProduct = async(e) => {
    e.preventDefault();
    const organisationId=localStorage.getItem("id");
    const product = {
      name: newProduct.name,
      category: newProduct.category,
      description: newProduct.description,
      price: parseFloat(newProduct.price) || 0,
      stockQuantity: parseInt(newProduct.stockQuantity) || 0,
      reorderLevel: parseInt(newProduct.reorderLevel) || 0,
      organisationId: organisationId ,
    };

   const result=await addNewProduct(product)
   console.log(result);

    setProducts([...products, result]);
    setNewProduct({
      name: "",
      category: "",
      description: "",
      price: "",
      stockQuantity: "",
      reorderLevel: ""
    });
    setShowAddForm(false);

    toast.success("Product added successfully!");
  };

  // Delete product
  const handleDelete = async(id) => {
    if (!window.confirm("Are you sure you want to delete this product?")) return;
  try {
    const result = await deleteProductApi(id);
    console.log("Product deleted:", result);
     setProducts(products.filter(product => product.id !== id));
      toast.success("Product deleted successfully!");
  } catch (error) {
    alert("Failed to delete product: " + error.message);
  }
  };

  // Check if stock is low
  const isLowStock = (product) => {
    return product.stockQuantity <= product.reorderLevel;
  };

  return (
    <>
    <Navbar></Navbar>
    <div className="inventory-container">
      <div className="inventory-header">
        <h1>Inventory Management</h1>
        <button 
          className="btn-primary"
          onClick={() => setShowAddForm(true)}
        >
          + Add New Product
        </button>
      </div>

      {/* Add Product Form */}
      {showAddForm && (
        <div className="add-product-modal">
          <div className="modal-content">
            <div className="modal-header">
              <h3>Add New Product</h3>
              <button 
                className="close-btn"
                onClick={() => setShowAddForm(false)}
              >
                ×
              </button>
            </div>
            <form onSubmit={handleAddProduct}>
              <div className="form-grid">
                <div className="form-group">
                  <label>Name *</label>
                  <input
                    type="text"
                    name="name"
                    value={newProduct.name}
                    onChange={handleNewProductChange}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Category *</label>
                  <input
                    type="text"
                    name="category"
                    value={newProduct.category}
                    onChange={handleNewProductChange}
                    required
                  />
                </div>
                <div className="form-group full-width">
                  <label>Description</label>
                  <textarea
                    name="description"
                    value={newProduct.description}
                    onChange={handleNewProductChange}
                    rows="3"
                  />
                </div>
                <div className="form-group">
                  <label>Price *</label>
                  <input
                    type="number"
                    step="0.01"
                    name="price"
                    value={newProduct.price}
                    onChange={handleNewProductChange}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Stock Quantity *</label>
                  <input
                    type="number"
                    name="stockQuantity"
                    value={newProduct.stockQuantity}
                    onChange={handleNewProductChange}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Reorder Level *</label>
                  <input
                    type="number"
                    name="reorderLevel"
                    value={newProduct.reorderLevel}
                    onChange={handleNewProductChange}
                    required
                  />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn-secondary" onClick={() => setShowAddForm(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn-primary">
                  Add Product
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Inventory Table */}
      <div className="table-container">
        <table className="inventory-table">
          <thead>
            <tr>
              <th>Id</th>
              <th>Name</th>
              <th>Category</th>
              <th>Description</th>
              <th>Price</th>
              <th>Stock Quantity</th>
              <th>Min Stock Level</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map(product => (
              <tr key={product.id} className={isLowStock(product) ? 'low-stock' : ''}>
                {editingId === product.id ? (
                  <>
                                      
                     <td>#{product.id}</td>
                    
                    <td>
                      <input
                        type="text"
                        name="name"
                        value={editForm.name}
                        onChange={handleEditChange}
                        className="edit-input"
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        name="category"
                        value={editForm.category}
                        onChange={handleEditChange}
                        className="edit-input"
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        name="description"
                        value={editForm.description}
                        onChange={handleEditChange}
                        className="edit-input"
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        step="0.01"
                        name="price"
                        value={editForm.price}
                        onChange={handleEditChange}
                        className="edit-input"
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        name="stockQuantity"
                        value={editForm.stockQuantity}
                        onChange={handleEditChange}
                        className="edit-input"
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        name="reorderLevel"
                        value={editForm.reorderLevel}
                        onChange={handleEditChange}
                        className="edit-input"
                      />
                    </td>
                    <td>
                      <span className={`status ${isLowStock(editForm) ? 'low' : 'ok'}`}>
                        {isLowStock(editForm) ? 'Low Stock' : 'In Stock'}
                      </span>
                    </td>
                    <td>
                      <button 
                        className="btn-save"
                        onClick={() => handleSave(product.id)}
                      >
                        ✓
                      </button>
                      <button 
                        className="btn-cancel"
                        onClick={handleCancel}
                      >
                        ✗
                      </button>
                    </td>
                  </>
                ) : (
                  <>
                    <td>#{product.id}</td>
                    <td>{product.name}</td>
                    <td>{product.category}</td>
                    <td>{product.description}</td>
                    <td>${product.price.toFixed(2)}</td>
                    <td>{product.stockQuantity}</td>
                    <td>{product.reorderLevel}</td>
                    <td>
                      <span className={`status ${isLowStock(product) ? 'low' : 'ok'}`}>
                        {isLowStock(product) ? 'Low Stock' : 'In Stock'}
                      </span>
                    </td>
                    <td>
                      <button 
                        className="btn-edit"
                        onClick={() => handleEdit(product)}
                      >
                        Edit
                      </button>
                      <button 
                        className="btn-delete"
                        onClick={() => handleDelete(product.id)}
                      >
                        Delete
                      </button>
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
        
        {products.length === 0 && (
          <div className="empty-state">
            <p>No products found. Add your first product!</p>
          </div>
        )}
      </div>
    </div>

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
};

export default InventoryTable;