import React, { useRef, useState } from "react";
import { toast } from "react-toastify";
import "./FileUploadWithjson.css"; 

const FileUploadWithjson = () => {
    const fileInputRefs = useRef([]);
  const [fileDetails, setFileDetails] = useState([{ file: null, description: "" }]);

  const handleFileChange = (e, index) => {
    const newDetails = [...fileDetails];
    newDetails[index].file = e.target.files[0];
    setFileDetails(newDetails);
  };

  const handleDescriptionChange = (e, index) => {
    const newDetails = [...fileDetails];
    newDetails[index].description = e.target.value;
    setFileDetails(newDetails);
  };

  const addMoreFields = () => {
    setFileDetails([...fileDetails, { file: null, description: "" }]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const organizationId = localStorage.getItem("id");

    if (!organizationId || !fileDetails[0].file) {
      toast.error("Please select at least one file!");
      return;
    }

    const formData = new FormData();
    formData.append("organizationId", organizationId);

    fileDetails.forEach((item, index) => {
      formData.append(`fileDetails[${index}].file`, item.file);
      formData.append(`fileDetails[${index}].description`, item.description);
    });

    try {
      const token = localStorage.getItem("token");
      if (!token) throw new Error("No authentication token found");

      const response = await fetch("http://localhost:8080/upload/v2", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      const result = await response.text();
      console.log("file name: ",fileDetails)
      setFileDetails([{ file: null, description: "" }]);
       fileInputRefs.current.forEach((input) => {
      if (input) input.value = "";
    });
      console.log("file name2: ",fileDetails)
      toast.success("Files uploaded successfully!");
    } catch (error) {
      console.error("Error:", error);
      toast.error("File upload failed!");
    }
  };

  return (
    <div className="upload-container">
      <h2 className="title">Upload Files with Description</h2>

      <form onSubmit={handleSubmit} className="upload-form">
        {fileDetails.map((item, index) => (
          <div key={index} className="file-group">
            <input
              type="file"
              className="file-input"
              ref={(el) => (fileInputRefs.current[index] = el)}
              onChange={(e) => handleFileChange(e, index)}
              required
            />
            <input
              type="text"
              className="desc-input"
              placeholder="Enter description..."
              value={item.description}
              onChange={(e) => handleDescriptionChange(e, index)}
              required
            />
          </div>
        ))}

        <div className="button-group">
          {/* <button type="button" onClick={addMoreFields} className="add-btn">  + Add More </button> */}
          <button type="submit" className="upload-btn">
            Upload
          </button>
        </div>
      </form>
    </div>
  );
};

export default FileUploadWithjson;
