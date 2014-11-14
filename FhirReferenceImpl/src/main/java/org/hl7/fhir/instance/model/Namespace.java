package org.hl7.fhir.instance.model;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Tue, Oct 21, 2014 07:07+1100 for FHIR v0.3.0

import java.util.*;

/**
 * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data types.
 */
public class Namespace extends Resource {

    public enum NamespaceType {
        CODESYSTEM, // The namespace is used to define concepts and symbols to represent those concepts.  E.g. UCUM, LOINC, NDC code, local lab codes, etc.
        IDENTIFIER, // The namespace is used to manage identifiers (e.g. license numbers, order numbers, etc.).
        ROOT, // The namespace is used as the root for other identifiers and namespaces.
        NULL; // added to help the parsers
        public static NamespaceType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("codesystem".equals(codeString))
          return CODESYSTEM;
        if ("identifier".equals(codeString))
          return IDENTIFIER;
        if ("root".equals(codeString))
          return ROOT;
        throw new Exception("Unknown NamespaceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CODESYSTEM: return "codesystem";
            case IDENTIFIER: return "identifier";
            case ROOT: return "root";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CODESYSTEM: return "The namespace is used to define concepts and symbols to represent those concepts.  E.g. UCUM, LOINC, NDC code, local lab codes, etc.";
            case IDENTIFIER: return "The namespace is used to manage identifiers (e.g. license numbers, order numbers, etc.).";
            case ROOT: return "The namespace is used as the root for other identifiers and namespaces.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CODESYSTEM: return "";
            case IDENTIFIER: return "";
            case ROOT: return "";
            default: return "?";
          }
        }
    }

  public static class NamespaceTypeEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("codesystem".equals(codeString))
          return NamespaceType.CODESYSTEM;
        if ("identifier".equals(codeString))
          return NamespaceType.IDENTIFIER;
        if ("root".equals(codeString))
          return NamespaceType.ROOT;
        throw new Exception("Unknown NamespaceType code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == NamespaceType.CODESYSTEM)
        return "codesystem";
      if (code == NamespaceType.IDENTIFIER)
        return "identifier";
      if (code == NamespaceType.ROOT)
        return "root";
      return "?";
      }
    }

    public enum NamespaceStatus {
        PROPOSED, // System has been submitted but not yet approved.
        ACTIVE, // System is valid for use.
        RETIRED, // System should no longer be used.
        NULL; // added to help the parsers
        public static NamespaceStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("retired".equals(codeString))
          return RETIRED;
        throw new Exception("Unknown NamespaceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "System has been submitted but not yet approved.";
            case ACTIVE: return "System is valid for use.";
            case RETIRED: return "System should no longer be used.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "";
            case ACTIVE: return "";
            case RETIRED: return "";
            default: return "?";
          }
        }
    }

  public static class NamespaceStatusEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return NamespaceStatus.PROPOSED;
        if ("active".equals(codeString))
          return NamespaceStatus.ACTIVE;
        if ("retired".equals(codeString))
          return NamespaceStatus.RETIRED;
        throw new Exception("Unknown NamespaceStatus code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == NamespaceStatus.PROPOSED)
        return "proposed";
      if (code == NamespaceStatus.ACTIVE)
        return "active";
      if (code == NamespaceStatus.RETIRED)
        return "retired";
      return "?";
      }
    }

    public enum NamespaceIdentifierType {
        OID, // An ISO object identifier.  E.g. 1.2.3.4.5.
        UUID, // A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.
        URI, // A uniform resource identifier (ideally a URL - uniform resource locator).  E.g. http://unitsofmeasure.org.
        OTHER, // Some other type of unique identifier.  E.g HL7-assigned reserved string such as LN for LOINC.
        NULL; // added to help the parsers
        public static NamespaceIdentifierType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("oid".equals(codeString))
          return OID;
        if ("uuid".equals(codeString))
          return UUID;
        if ("uri".equals(codeString))
          return URI;
        if ("other".equals(codeString))
          return OTHER;
        throw new Exception("Unknown NamespaceIdentifierType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OID: return "oid";
            case UUID: return "uuid";
            case URI: return "uri";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OID: return "An ISO object identifier.  E.g. 1.2.3.4.5.";
            case UUID: return "A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.";
            case URI: return "A uniform resource identifier (ideally a URL - uniform resource locator).  E.g. http://unitsofmeasure.org.";
            case OTHER: return "Some other type of unique identifier.  E.g HL7-assigned reserved string such as LN for LOINC.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OID: return "";
            case UUID: return "";
            case URI: return "";
            case OTHER: return "";
            default: return "?";
          }
        }
    }

  public static class NamespaceIdentifierTypeEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("oid".equals(codeString))
          return NamespaceIdentifierType.OID;
        if ("uuid".equals(codeString))
          return NamespaceIdentifierType.UUID;
        if ("uri".equals(codeString))
          return NamespaceIdentifierType.URI;
        if ("other".equals(codeString))
          return NamespaceIdentifierType.OTHER;
        throw new Exception("Unknown NamespaceIdentifierType code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == NamespaceIdentifierType.OID)
        return "oid";
      if (code == NamespaceIdentifierType.UUID)
        return "uuid";
      if (code == NamespaceIdentifierType.URI)
        return "uri";
      if (code == NamespaceIdentifierType.OTHER)
        return "other";
      return "?";
      }
    }

    public static class NamespaceUniqueIdComponent extends BackboneElement {
        /**
         * Identifies the unique identifier scheme used for this particular identifier.
         */
        protected Enumeration<NamespaceIdentifierType> type;

        /**
         * The string that should be sent over the wire to identify the code system or identifier system.
         */
        protected StringType value;

        /**
         * Indicates whether this identifier is the "preferred" identifier of this type.
         */
        protected BooleanType preferred;

        /**
         * Identifies the period of time over which this identifier is considered appropriate to refer to the namespace.  Outside of this window, the identifier might be non-deterministic.
         */
        protected Period period;

        private static final long serialVersionUID = 1593171318L;

      public NamespaceUniqueIdComponent() {
        super();
      }

      public NamespaceUniqueIdComponent(Enumeration<NamespaceIdentifierType> type, StringType value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (Identifies the unique identifier scheme used for this particular identifier.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<NamespaceIdentifierType> getTypeElement() { 
          return this.type;
        }

        /**
         * @param value {@link #type} (Identifies the unique identifier scheme used for this particular identifier.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public NamespaceUniqueIdComponent setTypeElement(Enumeration<NamespaceIdentifierType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Identifies the unique identifier scheme used for this particular identifier.
         */
        public NamespaceIdentifierType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Identifies the unique identifier scheme used for this particular identifier.
         */
        public NamespaceUniqueIdComponent setType(NamespaceIdentifierType value) { 
            if (this.type == null)
              this.type = new Enumeration<NamespaceIdentifierType>();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The string that should be sent over the wire to identify the code system or identifier system.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          return this.value;
        }

        /**
         * @param value {@link #value} (The string that should be sent over the wire to identify the code system or identifier system.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public NamespaceUniqueIdComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The string that should be sent over the wire to identify the code system or identifier system.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The string that should be sent over the wire to identify the code system or identifier system.
         */
        public NamespaceUniqueIdComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @return {@link #preferred} (Indicates whether this identifier is the "preferred" identifier of this type.). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public BooleanType getPreferredElement() { 
          return this.preferred;
        }

        /**
         * @param value {@link #preferred} (Indicates whether this identifier is the "preferred" identifier of this type.). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public NamespaceUniqueIdComponent setPreferredElement(BooleanType value) { 
          this.preferred = value;
          return this;
        }

        /**
         * @return Indicates whether this identifier is the "preferred" identifier of this type.
         */
        public boolean getPreferred() { 
          return this.preferred == null ? false : this.preferred.getValue();
        }

        /**
         * @param value Indicates whether this identifier is the "preferred" identifier of this type.
         */
        public NamespaceUniqueIdComponent setPreferred(boolean value) { 
          if (value == false)
            this.preferred = null;
          else {
            if (this.preferred == null)
              this.preferred = new BooleanType();
            this.preferred.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #period} (Identifies the period of time over which this identifier is considered appropriate to refer to the namespace.  Outside of this window, the identifier might be non-deterministic.)
         */
        public Period getPeriod() { 
          return this.period;
        }

        /**
         * @param value {@link #period} (Identifies the period of time over which this identifier is considered appropriate to refer to the namespace.  Outside of this window, the identifier might be non-deterministic.)
         */
        public NamespaceUniqueIdComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Identifies the unique identifier scheme used for this particular identifier.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value", "string", "The string that should be sent over the wire to identify the code system or identifier system.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("preferred", "boolean", "Indicates whether this identifier is the 'preferred' identifier of this type.", 0, java.lang.Integer.MAX_VALUE, preferred));
          childrenList.add(new Property("period", "Period", "Identifies the period of time over which this identifier is considered appropriate to refer to the namespace.  Outside of this window, the identifier might be non-deterministic.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      public NamespaceUniqueIdComponent copy() {
        NamespaceUniqueIdComponent dst = new NamespaceUniqueIdComponent();
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        dst.preferred = preferred == null ? null : preferred.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

  }

    public static class NamespaceContactComponent extends BackboneElement {
        /**
         * Names of the person who can be contacted.
         */
        protected HumanName name;

        /**
         * Identifies the mechanism(s) by which they can be contacted.
         */
        protected List<ContactPoint> telecom = new ArrayList<ContactPoint>();

        private static final long serialVersionUID = 465097125L;

      public NamespaceContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (Names of the person who can be contacted.)
         */
        public HumanName getName() { 
          return this.name;
        }

        /**
         * @param value {@link #name} (Names of the person who can be contacted.)
         */
        public NamespaceContactComponent setName(HumanName value) { 
          this.name = value;
          return this;
        }

        /**
         * @return {@link #telecom} (Identifies the mechanism(s) by which they can be contacted.)
         */
        public List<ContactPoint> getTelecom() { 
          return this.telecom;
        }

    // syntactic sugar
        /**
         * @return {@link #telecom} (Identifies the mechanism(s) by which they can be contacted.)
         */
        public ContactPoint addTelecom() { 
          ContactPoint t = new ContactPoint();
          this.telecom.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "HumanName", "Names of the person who can be contacted.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Identifies the mechanism(s) by which they can be contacted.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      public NamespaceContactComponent copy() {
        NamespaceContactComponent dst = new NamespaceContactComponent();
        dst.name = name == null ? null : name.copy();
        dst.telecom = new ArrayList<ContactPoint>();
        for (ContactPoint i : telecom)
          dst.telecom.add(i.copy());
        return dst;
      }

  }

    /**
     * Indicates the purpose for the namespace - what kinds of things does it make unique?.
     */
    protected Enumeration<NamespaceType> type;

    /**
     * The descriptive name of this particular identifier type or code system.
     */
    protected StringType name;

    /**
     * Indicates whether the namespace is "ready for use" or not.
     */
    protected Enumeration<NamespaceStatus> status;

    /**
     * If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     */
    protected CodeType country;

    /**
     * Categorizes a namespace for easier search by grouping related namespaces.
     */
    protected CodeableConcept category;

    /**
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     */
    protected StringType responsible;

    /**
     * Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    protected StringType description;

    /**
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     */
    protected StringType usage;

    /**
     * Indicates how the system may be identified when referenced in electronic exchange.
     */
    protected List<NamespaceUniqueIdComponent> uniqueId = new ArrayList<NamespaceUniqueIdComponent>();

    /**
     * The person who can be contacted about this system registration entry.
     */
    protected NamespaceContactComponent contact;

    /**
     * For namespaces that are retired, indicates the namespace that should be used in their place (if any).
     */
    protected Reference replacedBy;

    /**
     * The actual object that is the target of the reference (For namespaces that are retired, indicates the namespace that should be used in their place (if any).)
     */
    protected Namespace replacedByTarget;

    private static final long serialVersionUID = 1011915778L;

    public Namespace() {
      super();
    }

    public Namespace(Enumeration<NamespaceType> type, StringType name, Enumeration<NamespaceStatus> status) {
      super();
      this.type = type;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #type} (Indicates the purpose for the namespace - what kinds of things does it make unique?.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<NamespaceType> getTypeElement() { 
      return this.type;
    }

    /**
     * @param value {@link #type} (Indicates the purpose for the namespace - what kinds of things does it make unique?.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Namespace setTypeElement(Enumeration<NamespaceType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Indicates the purpose for the namespace - what kinds of things does it make unique?.
     */
    public NamespaceType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Indicates the purpose for the namespace - what kinds of things does it make unique?.
     */
    public Namespace setType(NamespaceType value) { 
        if (this.type == null)
          this.type = new Enumeration<NamespaceType>();
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (The descriptive name of this particular identifier type or code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      return this.name;
    }

    /**
     * @param value {@link #name} (The descriptive name of this particular identifier type or code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Namespace setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The descriptive name of this particular identifier type or code system.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The descriptive name of this particular identifier type or code system.
     */
    public Namespace setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether the namespace is "ready for use" or not.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<NamespaceStatus> getStatusElement() { 
      return this.status;
    }

    /**
     * @param value {@link #status} (Indicates whether the namespace is "ready for use" or not.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Namespace setStatusElement(Enumeration<NamespaceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the namespace is "ready for use" or not.
     */
    public NamespaceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the namespace is "ready for use" or not.
     */
    public Namespace setStatus(NamespaceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<NamespaceStatus>();
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #country} (If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.). This is the underlying object with id, value and extensions. The accessor "getCountry" gives direct access to the value
     */
    public CodeType getCountryElement() { 
      return this.country;
    }

    /**
     * @param value {@link #country} (If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.). This is the underlying object with id, value and extensions. The accessor "getCountry" gives direct access to the value
     */
    public Namespace setCountryElement(CodeType value) { 
      this.country = value;
      return this;
    }

    /**
     * @return If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     */
    public String getCountry() { 
      return this.country == null ? null : this.country.getValue();
    }

    /**
     * @param value If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.
     */
    public Namespace setCountry(String value) { 
      if (value == null)
        this.country = null;
      else {
        if (this.country == null)
          this.country = new CodeType();
        this.country.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #category} (Categorizes a namespace for easier search by grouping related namespaces.)
     */
    public CodeableConcept getCategory() { 
      return this.category;
    }

    /**
     * @param value {@link #category} (Categorizes a namespace for easier search by grouping related namespaces.)
     */
    public Namespace setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #responsible} (The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.). This is the underlying object with id, value and extensions. The accessor "getResponsible" gives direct access to the value
     */
    public StringType getResponsibleElement() { 
      return this.responsible;
    }

    /**
     * @param value {@link #responsible} (The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.). This is the underlying object with id, value and extensions. The accessor "getResponsible" gives direct access to the value
     */
    public Namespace setResponsibleElement(StringType value) { 
      this.responsible = value;
      return this;
    }

    /**
     * @return The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     */
    public String getResponsible() { 
      return this.responsible == null ? null : this.responsible.getValue();
    }

    /**
     * @param value The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.
     */
    public Namespace setResponsible(String value) { 
      if (value == null)
        this.responsible = null;
      else {
        if (this.responsible == null)
          this.responsible = new StringType();
        this.responsible.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (Details about what the namespace identifies including scope, granularity, version labeling, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      return this.description;
    }

    /**
     * @param value {@link #description} (Details about what the namespace identifies including scope, granularity, version labeling, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Namespace setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Details about what the namespace identifies including scope, granularity, version labeling, etc.
     */
    public Namespace setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #usage} (Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      return this.usage;
    }

    /**
     * @param value {@link #usage} (Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public Namespace setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.
     */
    public Namespace setUsage(String value) { 
      if (value == null)
        this.usage = null;
      else {
        if (this.usage == null)
          this.usage = new StringType();
        this.usage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #uniqueId} (Indicates how the system may be identified when referenced in electronic exchange.)
     */
    public List<NamespaceUniqueIdComponent> getUniqueId() { 
      return this.uniqueId;
    }

    // syntactic sugar
    /**
     * @return {@link #uniqueId} (Indicates how the system may be identified when referenced in electronic exchange.)
     */
    public NamespaceUniqueIdComponent addUniqueId() { 
      NamespaceUniqueIdComponent t = new NamespaceUniqueIdComponent();
      this.uniqueId.add(t);
      return t;
    }

    /**
     * @return {@link #contact} (The person who can be contacted about this system registration entry.)
     */
    public NamespaceContactComponent getContact() { 
      return this.contact;
    }

    /**
     * @param value {@link #contact} (The person who can be contacted about this system registration entry.)
     */
    public Namespace setContact(NamespaceContactComponent value) { 
      this.contact = value;
      return this;
    }

    /**
     * @return {@link #replacedBy} (For namespaces that are retired, indicates the namespace that should be used in their place (if any).)
     */
    public Reference getReplacedBy() { 
      return this.replacedBy;
    }

    /**
     * @param value {@link #replacedBy} (For namespaces that are retired, indicates the namespace that should be used in their place (if any).)
     */
    public Namespace setReplacedBy(Reference value) { 
      this.replacedBy = value;
      return this;
    }

    /**
     * @return {@link #replacedBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (For namespaces that are retired, indicates the namespace that should be used in their place (if any).)
     */
    public Namespace getReplacedByTarget() { 
      return this.replacedByTarget;
    }

    /**
     * @param value {@link #replacedBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (For namespaces that are retired, indicates the namespace that should be used in their place (if any).)
     */
    public Namespace setReplacedByTarget(Namespace value) { 
      this.replacedByTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "Indicates the purpose for the namespace - what kinds of things does it make unique?.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("name", "string", "The descriptive name of this particular identifier type or code system.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "Indicates whether the namespace is 'ready for use' or not.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("country", "code", "If present, indicates that the identifier or code system is principally intended for use or applies to entities within the specified country.  For example, the country associated with a national code system.", 0, java.lang.Integer.MAX_VALUE, country));
        childrenList.add(new Property("category", "CodeableConcept", "Categorizes a namespace for easier search by grouping related namespaces.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("responsible", "string", "The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring their non-collision.", 0, java.lang.Integer.MAX_VALUE, responsible));
        childrenList.add(new Property("description", "string", "Details about what the namespace identifies including scope, granularity, version labeling, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("usage", "string", "Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower case, etc.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("uniqueId", "", "Indicates how the system may be identified when referenced in electronic exchange.", 0, java.lang.Integer.MAX_VALUE, uniqueId));
        childrenList.add(new Property("contact", "", "The person who can be contacted about this system registration entry.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("replacedBy", "Reference(Namespace)", "For namespaces that are retired, indicates the namespace that should be used in their place (if any).", 0, java.lang.Integer.MAX_VALUE, replacedBy));
      }

      public Namespace copy() {
        Namespace dst = new Namespace();
        dst.type = type == null ? null : type.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.country = country == null ? null : country.copy();
        dst.category = category == null ? null : category.copy();
        dst.responsible = responsible == null ? null : responsible.copy();
        dst.description = description == null ? null : description.copy();
        dst.usage = usage == null ? null : usage.copy();
        dst.uniqueId = new ArrayList<NamespaceUniqueIdComponent>();
        for (NamespaceUniqueIdComponent i : uniqueId)
          dst.uniqueId.add(i.copy());
        dst.contact = contact == null ? null : contact.copy();
        dst.replacedBy = replacedBy == null ? null : replacedBy.copy();
        return dst;
      }

      protected Namespace typedCopy() {
        return copy();
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Namespace;
   }


}

