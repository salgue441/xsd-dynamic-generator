package com.example.exception;

import java.util.Objects;

/**
 * A comprehensive exception class for handling errors during XSD (XML Schema
 * Definition) generation.
 * 
 * This exception provides:
 * - Detailed error messages
 * - Support for root cause tracking
 * - Additional context about the XSD generation failure
 */
public class XsdGenerationException extends Exception {
  public enum ErrorCode {
    SCHEMA_vALIDATION_FAILED,
    FILE_ACCESS_ERROR,
    PARSING_ERROR,
    INVALID_CONFIG,
    UNKNOWN_ERROR
  }

  private final ErrorCode errorCode;
  private final transient Object context;

  /**
   * Constructs a new XSD generation exception with a message and error code.
   * 
   * @param message   A descriptive message explaining the specified failure
   * @param errorCode The specific type of error that occurred
   * @version 1.0.0
   */
  public XsdGenerationException(String message, ErrorCode errorCode) {
    this(message, errorCode, null, null);
  }

  /**
   * Constructs a new XSD generation exception with a message, error code, and
   * cause.
   *
   * @param message   A descriptive message explaining the specific failure
   * @param errorCode The specific type of error that occurred
   * @param cause     The underlying cause of the exception
   * @version 1.0.0
   */
  public XsdGenerationException(String message, ErrorCode errorCode, Throwable cause) {
    this(message, errorCode, cause, null);
  }

  /**
   * Fully parameterized constructor for maximum flexibility.
   *
   * @param message   A descriptive message explaining the specific failure
   * @param errorCode The specific type of error that occurred
   * @param cause     The underlying cause of the exception
   * @param context   Additional context information for debugging
   * @version 1.0.0
   */
  public XsdGenerationException(
      String message,
      ErrorCode errorCode,
      Throwable cause,
      Object context) {
    super(
        buildDetailedMessage(message, errorCode, context),
        cause);

    this.errorCode = Objects.requireNonNull(errorCode);
    this.context = context;
  }

  /**
   * Builds a comprehensive error message with all available details.
   *
   * @param message   The base error message
   * @param errorCode The error code
   * @param context   Additional context information
   * @return A detailed, informative error message
   */
  private static String buildDetailedMessage(
      String message,
      ErrorCode errorCode,
      Object context) {
    StringBuilder detailedMessage = new StringBuilder();
    if (message != null) {
      detailedMessage.append(message);
    }

    if (errorCode != null) {
      if (detailedMessage.length() > 0) {
        detailedMessage.append(" | ");
      }

      detailedMessage.append("Error Code: ").append(errorCode);
    }

    if (context != null) {
      detailedMessage.append(" | Context: ").append(context);
    }

    return detailedMessage.toString();
  }

  /**
   * Retrieves the error code associated with this exception.
   *
   * @return The error code
   * @version 1.0.0
   */
  public ErrorCode getErrorCode() {
    return errorCode;
  }

  /**
   * Retrieves the additional context information associated
   * with this exception.
   *
   * @return The context information
   * @version 1.0.0
   */
  public Object getContext() {
    return context;
  }

  /**
   * Returns a string representation of this exception.
   *
   * @return A string representation of this exception
   * @version 1.0.0
   */
  @Override
  public String toString() {
    return String.format(
        "XsdGenerationException [errorCode=%s, message=%s]",
        errorCode,
        getMessage());
  }
}
