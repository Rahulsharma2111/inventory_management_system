import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import * as XLSX from "xlsx";
import "./FileManager.css";

const FileManager = () => {
  const [files, setFiles] = useState([]);
  const [editingFileId, setEditingFileId] = useState(null);
  const [newDescription, setNewDescription] = useState("");
  const [newFile, setNewFile] = useState("");
  const [filePreview, setFilePreview] = useState("");
  const [isModalOpenFilePreview, setIsModalOpenFilePreview] = useState(false);
  const [previewType, setPreviewType] = useState("");
  const [excelData, setExcelData] = useState([]);
  const [isExcelModal, setIsExcelModal] = useState(false);

  const organisationId = localStorage.getItem("id");
  const token = localStorage.getItem("token");

  // Fetch all files for organization
  const fetchFiles = async () => {
    try {
      const res = await fetch(`http://localhost:8080/files/${organisationId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      setFiles(data);
    } catch (err) {
      console.error(err);
      toast.error("Failed to fetch files");
    }
  };

  useEffect(() => {
    fetchFiles();
  }, []);

  // Download file
  const downloadFile = async (file) => {
    try {
      const res = await fetch(
        `http://localhost:8080/files?fileId=${file.id}&organisationId=${organisationId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      const blob = await res.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", file.fileName);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error(err);
      toast.error("Failed to download file");
    }
  };

  // Delete file
  const deleteFile = async (fileId) => {
    if (!window.confirm("Are you sure you want to delete this file?")) return;
    try {
      await fetch(`http://localhost:8080/${fileId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      toast.success("File deleted successfully");
      fetchFiles();
    } catch (err) {
      console.error(err);
      toast.error("Failed to delete file");
    }
  };

  //   const updateFileData = async (fileId) => {
  //     try {
  //       await fetch(`http://localhost:8080/${fileId}?description=${encodeURIComponent(newDescription)}`, {
  //         method: "PUT",
  //         headers: { Authorization: `Bearer ${token}` },
  //       });
  //       toast.success("file update updated");
  //       setEditingFileId(null);
  //       fetchFiles();
  //     } catch (err) {
  //       console.error(err);
  //       toast.error("Failed to update description");
  //     }
  //   };

  const updateFileData = async (fileId) => {
    const formData = new FormData();
    formData.append("description", newDescription);

    if (newFile) {
      formData.append("file", newFile);
    }

    try {
      const response = await fetch(`http://localhost:8080/${fileId}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) throw new Error("Failed");
      toast.success("Updated successfully");
      setEditingFileId(null);
      fetchFiles();
    } catch (err) {
      console.error(err);
      toast.error("Failed to update");
    }
  };

  // Preview file
  const previewFile = async (file) => {
    try {
      const res = await fetch(
        `http://localhost:8080/files?fileId=${file.id}&organisationId=${organisationId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      const blob = await res.blob();
      const fileType = blob.type;
      console.log(fileType);
      setPreviewType(fileType);
       if (
      fileType === "application/vnd.ms-excel" ||
      fileType === "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    ) {
      const arrayBuffer = await blob.arrayBuffer();
      const workbook = XLSX.read(arrayBuffer, { type: "array" });

      // Read first sheet only
      const sheetName = workbook.SheetNames[0];
      const sheetData = XLSX.utils.sheet_to_json(workbook.Sheets[sheetName], {
        header: 1,
      });

      setExcelData(sheetData); 
      setIsExcelModal(true);
    } else {
      // Image / PDF Preview
      const url = URL.createObjectURL(blob);
      setFilePreview(url);
      setIsModalOpenFilePreview(true);
    }
    } catch (err) {
      console.error(err);
      toast.error("Failed to load file");
    }
  };

  return (
    <div className="file-manager">
      <h2>Organisation Files</h2>
      {files.length === 0 && <p>No files uploaded yet.</p>}

      {/* Model for csv or excel file */}
      {isExcelModal && (
  <div className="modal">
    <div className="modal-content-data">
      <h3>Excel File Preview</h3>
      <button onClick={() => setIsExcelModal(false)}>X</button>

      <table className="excel-table">
        <tbody>
          {excelData.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map((cell, cellIndex) => (
                <td key={cellIndex}>{cell}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
)}

      {/* file preview model */}
      {isModalOpenFilePreview && (
        <div
          className="modal-overlay"
          onClick={() => setIsModalOpenFilePreview(false)}
        >
          <div className="modal-content-data" onClick={(e) => e.stopPropagation()}>
            {/* <img src={filePreview} alt="Preview" className="preview-image" /> */}
            {previewType.startsWith("image/") ? (
              <img src={filePreview} alt="Preview" className="preview-image" />
            ) : (
              <iframe
                src={filePreview}
                title="File Preview"
                className="preview-iframe"
              ></iframe>
            )}

            <button
              className="close-btn"
              onClick={() => setIsModalOpenFilePreview(false)}
            >
              ✖
            </button>
          </div>
        </div>
      )}

      <table>
        <thead>
          <tr>
            <th>#Id</th>
            <th>File Name</th>
            <th>Description</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {files.map((file, index) => (
            <tr key={file.id}>
              <td>#{file.id}</td>
              {/* <td>{file.fileName}</td> */}
              <td>
                {editingFileId === file.id ? (
                  <input
                    type="file"
                    // value={file.fileName}
                    onChange={(e) => setNewFile(e.target.files[0])}
                  />
                ) : (
                  file.fileName
                )}
              </td>

              <td>
                {editingFileId === file.id ? (
                  <input
                    type="text"
                    value={newDescription}
                    onChange={(e) => setNewDescription(e.target.value)}
                  />
                ) : (
                  file.description
                )}
              </td>
              <td>
                {/* <button onClick={() => downloadFile(file)}>Download</button> */}
                <button
                  onClick={() => previewFile(file)}
                  className="btn-preview"
                >
                  Preview
                </button>

                {editingFileId === file.id ? (
                  <button
                    onClick={() => updateFileData(file.id)}
                    className="btn-save"
                  >
                    Save
                  </button>
                ) : (
                  <button
                    className="btn-edit"
                    onClick={() => {
                      setEditingFileId(file.id);
                      setNewDescription(file.description);
                    }}
                  >
                    Edit
                  </button>
                )}
                <button
                  className="btn-delete"
                  onClick={() => deleteFile(file.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default FileManager;
