package io.github.nthportal.version;

public class VersionFormatException extends IllegalArgumentException {
    private static final String messagePrefix = "Invalid version string: ";

    VersionFormatException(String versionString) {
        super(messagePrefix + versionString);
    }

    VersionFormatException(String versionString, String message) {
        super(messagePrefix + versionString + "; " + message);
    }

    VersionFormatException(String versionString, Throwable cause) {
        super(messagePrefix + versionString, cause);
    }
}