/**
 * Dummy exception to be thrown in test and make sure that the SUT don't wrap the exceptions
 */
class CustomException(message: String) : Exception(message)
