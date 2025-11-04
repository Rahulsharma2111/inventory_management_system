package com.mis.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class CustomResponse {

    public static <T> ResponseEntity<Map> ok(T data){

        Map<String, Object> metaInfo = new HashMap<>();
        metaInfo.put("message","SUCCESS");
        metaInfo.put("statusCode",200);
        metaInfo.put("error",false);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    public static <T> ResponseEntity<Map> ok(String message,T data){

        Map<String, Object> metaInfo = new HashMap<>();
        metaInfo.put("message",message);
        metaInfo.put("statusCode",200);
        metaInfo.put("error",false);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Map> internalServerError(T data){
        Map<String, Object> metaInfo=new HashMap<>();
        metaInfo.put("message","Internal Server Error");
        metaInfo.put("statusCode","500");
        metaInfo.put("error","true");

        Map<String,Object> responseData=new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", data);
        return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public static <T> ResponseEntity<Map> internalServerError(String message){
        Map<String, Object> metaInfo=new HashMap<>();
        metaInfo.put("message",message);
        metaInfo.put("statusCode","500");
        metaInfo.put("error","true");

        Map<String,Object> responseData=new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", "");
        return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseEntity<Map> internalServerError(String message,T data){
        Map<String, Object> metaInfo=new HashMap<>();
        metaInfo.put("message",message);
        metaInfo.put("statusCode","500");
        metaInfo.put("error","true");

        Map<String,Object> responseData=new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", data);
        return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public static <T> ResponseEntity<Map> created(String message){
        Map<String, Object> metaInfo=new HashMap<>();
        metaInfo.put("message",message);
        metaInfo.put("statusCode","401");
        metaInfo.put("error","false");

        Map<String,Object> responseData=new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", "");
        return new ResponseEntity<>(responseData,HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<Map> created(String message,T data){
        Map<String, Object> metaInfo=new HashMap<>();
        metaInfo.put("message",message);
        metaInfo.put("statusCode","401");
        metaInfo.put("error","false");

        Map<String,Object> responseData=new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", data);
        return new ResponseEntity<>(responseData,HttpStatus.CREATED);
    }


    public static <T> ResponseEntity<Map> badRequest(String message){
        Map<String, Object> metaInfo=new HashMap<>();
        metaInfo.put("message",message);
        metaInfo.put("statusCode","400");
        metaInfo.put("error","true");

        Map<String,Object> responseData=new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", "");
        return new ResponseEntity<>(responseData,HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<Map> badRequest(String message,T data){
        Map<String, Object> metaInfo=new HashMap<>();
        metaInfo.put("message",message);
        metaInfo.put("statusCode","400");
        metaInfo.put("error","true");

        Map<String,Object> responseData=new HashMap<>();
        responseData.put("meta", metaInfo);
        responseData.put("data", data);
        return new ResponseEntity<>(responseData,HttpStatus.BAD_REQUEST);
    }
}
