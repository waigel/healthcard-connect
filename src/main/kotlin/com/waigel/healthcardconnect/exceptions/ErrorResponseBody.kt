package com.waigel.healthcardconnect.exceptions

import java.io.Serializable

class ErrorResponseBody(var code: String, var params: List<Serializable>?)