package com.mis.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(NegativeValueHandleException.class)
    public ResponseEntity<?> negativeValueHandleException(NegativeValueHandleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExist.class)
    public ResponseEntity<?> resourceAlreadyExist(NegativeValueHandleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HandleRuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(HandleRuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage()));
        // use your CustomResponse style
        Map<String, Object> meta = new HashMap<>();
        meta.put("message", "Validation Failed");
        meta.put("statusCode", HttpStatus.BAD_REQUEST.value());
        meta.put("error", true);

        Map<String, Object> response = new HashMap<>();
        response.put("meta", meta);
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//
//        Map<String, String> validationErrors = new HashMap<>();
//
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            validationErrors.put(fieldName, errorMessage);
//        });
//
//        // Meta info structure (same as your ok() method)
//        Map<String, Object> metaInfo = new HashMap<>();
//        metaInfo.put("message", "Validation Failed");
//        metaInfo.put("statusCode", HttpStatus.BAD_REQUEST.value());
//        metaInfo.put("error", true);
//
//        // Response structure
//        Map<String, Object> response = new HashMap<>();
//        response.put("meta", metaInfo);
//        response.put("data", validationErrors);
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

}






















//
//@ControllerAdvice
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    // Handle Resource Not Found
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleResourceNotFound(
//            ResourceNotFoundException ex, WebRequest request) {
//
//        ErrorResponse errorResponse = new ErrorResponse(
//            HttpStatus.NOT_FOUND.value(),
//            ex.getMessage(),
//            new Date(),
//            request.getDescription(false)
//        );
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//    }
//
//    // Handle Business Exceptions
//    @ExceptionHandler(BusinessException.class)
//    public ResponseEntity<ErrorResponse> handleBusinessException(
//            BusinessException ex, WebRequest request) {
//
//        ErrorResponse errorResponse = new ErrorResponse(
//            HttpStatus.BAD_REQUEST.value(),
//            ex.getMessage(),
//            new Date(),
//            request.getDescription(false)
//        );
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    // Handle Validation Exceptions
//    @ExceptionHandler(ValidationException.class)
//    public ResponseEntity<ErrorResponse> handleValidationException(
//            ValidationException ex, WebRequest request) {
//
//        ErrorResponse errorResponse = new ErrorResponse(
//            HttpStatus.BAD_REQUEST.value(),
//            ex.getMessage(),
//            new Date(),
//            request.getDescription(false)
//        );
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    // Handle Method Argument Not Valid (Bean Validation)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//
//        List<ErrorResponse.ValidationError> errors = ex.getBindingResult()
//            .getFieldErrors()
//            .stream()
//            .map(error -> new ErrorResponse.ValidationError(
//                error.getField(),
//                error.getDefaultMessage()))
//            .collect(Collectors.toList());
//
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//        errorResponse.setMessage("Validation failed");
//        errorResponse.setTimestamp(new Date());
//        errorResponse.setValidationErrors(errors);
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    // Handle Data Integrity Violation (DB constraints)
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
//            DataIntegrityViolationException ex, WebRequest request) {
//
//        ErrorResponse errorResponse = new ErrorResponse(
//            HttpStatus.CONFLICT.value(),
//            "Data integrity violation occurred",
//            new Date(),
//            request.getDescription(false)
//        );
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
//    }
//
//    // Handle Access Denied
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorResponse> handleAccessDenied(
//            AccessDeniedException ex, WebRequest request) {
//
//        ErrorResponse errorResponse = new ErrorResponse(
//            HttpStatus.FORBIDDEN.value(),
//            "Access denied",
//            new Date(),
//            request.getDescription(false)
//        );
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }
//
//    // Handle All Other Exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGlobalException(
//            Exception ex, WebRequest request) {
//
//        ErrorResponse errorResponse = new ErrorResponse(
//            HttpStatus.INTERNAL_SERVER_ERROR.value(),
//            "An unexpected error occurred",
//            new Date(),
//            request.getDescription(false)
//        );
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}










//@Service
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    public Order getOrderById(Long id) {
//        return orderRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
//    }
//
//    public Order createOrder(Order order) {
//        // Business logic validation
//        if (order.getQuantity() <= 0) {
//            throw new BusinessException("Order quantity must be greater than 0");
//        }
//
//        return orderRepository.save(order);
//    }
//}