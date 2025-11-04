import React, { useState } from "react";
import "./FileUpload.css"; 
import { toast } from "react-toastify";
import FileUploadWithjson from "../FileWithJSON/FileUploadWithjson";
import Navbar from "../Navbar/Navbar";

const FileUpload = () => {
  const [files, setFiles] = useState([]);

  const handleFileChange = (e) => {
    setFiles(e.target.files);
  };

  const handleUpload = async () => {
    const organisationId = localStorage.getItem("id");

    if (!organisationId || files.length === 0) {
      toast.error("Please select files ");
      return;
    }

    const formData = new FormData();
    formData.append("organizationId", organisationId);

    for (let i = 0; i < files.length; i++) {
      formData.append("files", files[i]);
    }

    try {
      const token = localStorage.getItem("token");
      if (!token) throw new Error("No authentication token found");

      const response = await fetch("http://localhost:8080/upload/v1", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (response.ok) {
        toast.success("Files Uploaded Successfully");
        setFiles([]); 
      } else {
        toast.error("Fail to upload");
      }
    } catch (error) {
      console.error("Error uploading files:", error);
     toast.error("Fail to upload ❌");
    }
  };

  return (
    <div className="upload-file-container">
       <Navbar></Navbar>
      <h3 className="upload-title">Upload Files</h3>

      <div className="file-input-wrapper">
        <input
          type="file"
          multiple
          id="fileInput"
          onChange={handleFileChange}
        />
        <label htmlFor="fileInput">
          {files.length > 0
            ? `${files.length} file(s) selected`
            : "Choose Files"}
        </label>
      </div>

      <button className="upload-btn" onClick={handleUpload}>
        Upload
      </button>
    <br></br>
    <br></br>
    <br></br>
    <br></br>
    <br></br>
    <br></br>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <hr></hr>
      <FileUploadWithjson></FileUploadWithjson>
    </div>
  );
};

export default FileUpload;
