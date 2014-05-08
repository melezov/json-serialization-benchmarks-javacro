package com.javacro.dslplatform.model.Accounting;

public final class Profile implements java.io.Serializable {
    public Profile(
            final String email,
            final String phoneNumber) {
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public Profile() {}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + 1346342343;
        result = prime * result
                + (this.email != null ? this.email.hashCode() : 0);
        result = prime * result
                + (this.phoneNumber != null ? this.phoneNumber.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (!(obj instanceof Profile)) return false;
        final Profile other = (Profile) obj;

        if (!(this.email == other.email || this.email != null
                && this.email.equals(other.email))) return false;
        if (!(this.phoneNumber == other.phoneNumber || this.phoneNumber != null
                && this.phoneNumber.equals(other.phoneNumber))) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Profile(" + email + ',' + phoneNumber + ')';
    }

    private static final long serialVersionUID = 0x0097000a;

    private String email;

    @com.fasterxml.jackson.annotation.JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public Profile setEmail(final String value) {
        this.email = value;

        return this;
    }

    private String phoneNumber;

    @com.fasterxml.jackson.annotation.JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Profile setPhoneNumber(final String value) {
        this.phoneNumber = value;

        return this;
    }
}
