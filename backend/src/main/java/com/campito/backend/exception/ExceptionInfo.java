package com.campito.backend.exception;

public record ExceptionInfo(String message, String path, String timestamp, int status) {}
