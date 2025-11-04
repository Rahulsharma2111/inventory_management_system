import { toast } from "react-toastify";

const baseUrl = "http://localhost:8080";

export const registerOrganizationApi = async (organizationData) => {
  const apiUrl = `${baseUrl}/organisation/register`;

  try {
    const response = await fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(organizationData),
    });

    const result = await response.json();

    if (!response.ok) {
      toast.error(result.meta.message);
      //  toast.error("Registration failed. Please try again.");
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return result.data;
  } catch (error) {
    toast.error(result.meta.message);
    console.error("Error registering organization:", error);
    throw error;
  }
};

export const loginApi = async (loginData) => {
  const apiUrl = `${baseUrl}/organisation/login`;

  try {
    const response = await fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginData),
    });

    const result = await response.json();
    if (!response.ok) {
      toast.error(result.meta.message);
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return result.data;
  } catch (error) {
    console.error("Error login :", error);
    throw error;
  }
};

export const addNewProduct = async (newProduct) => {
  const apiUrl = `${baseUrl}/product/add`;

  try {
    const token = localStorage.getItem("token");
    if (!token) {
      throw new Error("No authentication token found");
    }
    const response = await fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(newProduct),
    });
    console.log(newProduct);

    const result = await response.json();
    if (!response.ok) {
      console.log(`${result.meta.message}`);
      toast.error(`${result.meta.message}`);
      throw new Error(`${result.meta.message}`);
    }

    return result.data;
  } catch (error) {
    console.log(`ERROR TIME ${result.meta.message}`);
    console.error("Error registering organization:", error);
    throw error;
  }
};

export const uppdateProductApi = async (id, updateProductData) => {
  const apiUrl = `${baseUrl}/product/${id}`;

  try {
    const productRequest = {
      name: updateProductData.name,
      category: updateProductData.category,
      description: updateProductData.description,
      price: updateProductData.price,
      stockQuantity: updateProductData.stockQuantity,
      reorderLevel: updateProductData.reorderLevel,
      organisationId: updateProductData.organisationId,
    };

    const token = localStorage.getItem("token");
    if (!token) {
      throw new Error("No authentication token found");
    }
    const response = await fetch(apiUrl, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(productRequest),
    });

    const result = await response.json();

    console.log("result", result);

    if (!response.ok) {
      // toast.error(`${result.meta.message}`)
      // throw new Error(`${result.meta.message}`);
      if (result?.errors) {
        //  show only one error message
        const firstError = Object.values(result.errors)[0];
        toast.error(firstError);
        throw new Error(firstError);
      } else if (result?.meta?.message) {
        toast.error(result.meta.message);
        throw new Error(result.meta.message);
      } else {
        toast.error("Something went wrong!");
        throw new Error("Something went wrong!");
      }
    }
    return result;
  } catch (error) {
    console.error("Error update while product:", error);
    throw error;
  }
};

// export const getAllProductApi=async ()=>{
//     const apiUrl = `${baseUrl}/product`;

//   try {
//     const response = await fetch(apiUrl);

//     if (!response.ok) {
//       throw new Error(`HTTP error! status: ${response.status}`);
//     }

//     const result = await response.json();
//     return result;
//   } catch (error) {
//     console.error("Error registering organization:", error);
//     throw error;
//   }
// };

export const getAllProductApi = async (organisationId) => {
  const apiUrl = `${baseUrl}/product/${organisationId}`;

  try {
    const token = localStorage.getItem("token");
    // console.log("token",token)

    if (!token) {
      toast.error("Please login!");
      throw new Error("No authentication token found");
    }

    const response = await fetch(apiUrl, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      if (response.status === 401) {
        localStorage.removeItem("jwtToken");
        throw new Error("Authentication failed. Please login again.");
      }
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result.data;
  } catch (error) {
    console.error("Error fetching products:", error);
    throw error;
  }
};

export const createOrderApi = async (orderData) => {
  const apiUrl = `${baseUrl}/order/create`;

  try {
    const token = localStorage.getItem("token");
    if (!token) {
      throw new Error("No authentication token found");
    }
    const response = await fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(orderData),
    });

    const result = await response.json();
    if (!response.ok) {
      toast.error(`${result.meta.message} `);
      console.log(result.meta.message);
      throw new Error(result.meta.message);
    }

    return result.data;
  } catch (error) {
    console.error("Error :", error);
    throw error;
  }
};

export const getAllOrderByOrganisationIdApi = async (organisationId) => {
  const apiUrl = `${baseUrl}/order/${organisationId}`;
  const token = localStorage.getItem("token");
  if (!token) {
    throw new Error("No authentication token found");
  }
  try {
    const response = await fetch(apiUrl, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result.data;
  } catch (error) {
    console.error("Error :", error);
    throw error;
  }
};

export const getDashboardStatsApi = async () => {
  const organisationId = localStorage.getItem("id");
  console.log(organisationId);
  const apiUrl = `${baseUrl}/dashboard/${organisationId}`;
  const token = localStorage.getItem("token");
  if (!token) {
    throw new Error("No authentication token found");
  }
  try {
    const response = await fetch(apiUrl, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result.data;
  } catch (error) {
    console.error("Error: ", error);
    throw error;
  }
};

export const deleteProductApi = async (productId) => {
  const apiUrl = `${baseUrl}/product/${productId}`;

  try {
    const token = localStorage.getItem("token");
    if (!token) {
      throw new Error("No authentication token found");
    }

    const response = await fetch(apiUrl, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      if (response.status === 401) {
        localStorage.removeItem("jwtToken");
        throw new Error("Authentication failed. Please login again.");
      }
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.text();
    return result;
  } catch (error) {
    console.error("Error deleting product:", error);
    throw error;
  }
};

export const uploadImageApi = async (file) => {
  const organisationId = localStorage.getItem("id");
  const apiUrl = `${baseUrl}/organisation/upload/${organisationId}`;

  try {
    const token = localStorage.getItem("token");
    if (!token) throw new Error("No authentication token found");

    const formData = new FormData();
    formData.append("file", file);

    const response = await fetch(apiUrl, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData,
    });

    const result = await response.json();
    console.log(result);
    if (!response.ok) {
      console.error("Upload error:", result);
      throw new Error(result.meta?.message || "Upload failed");
    }

    return result.data;
  } catch (error) {
    console.error("Upload failed:", error);
    throw error;
  }
};

export const getLogoImageApi = async () => {
  const organisationId = localStorage.getItem("id");
  const apiUrl = `${baseUrl}/organisation/${organisationId}`;
  const token = localStorage.getItem("token");

  if (!token) {
    throw new Error("No authentication token found");
  }
  try {
    const response = await fetch(apiUrl, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTILff8WQXlbQaCjWjc-teAFi7xmVFW2Ggb5g&s";
      // toast.error("Fail to load logo image");
      // throw new Error(`HTTP error! status: ${response.status}`);
    }

      const blob = await response.blob();
    const imageUrl = URL.createObjectURL(blob);
    // toast.success("Successfully image loaded");
    return imageUrl;
    
  } catch (error) {
    console.error("Error : ", error);
    throw error;
  }
};


export const deleteLogoApi = async () => {
  
  const organisationId = localStorage.getItem("id");
  const apiUrl = `${baseUrl}/organisation/${organisationId}`;

  try {
    const token = localStorage.getItem("token");
    if (!token) {
      throw new Error("No authentication token found");
    }

    const response = await fetch(apiUrl, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

      const result = await response.json();
    if (!response.ok) {
      if (response.status === 401) {
        toast.error(result.meta.message)
        throw new Error("Authentication failed. Please login again.");
      }
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return result;
  } catch (error) {
    console.error("Error deleting product:", error);
    throw error;
  }
};

