//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.28 at 06:36:54 PM CET 
//


package com.enonic.wem.api.xml.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataPropertyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="dataPropertyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Boolean"/>
 *     &lt;enumeration value="String"/>
 *     &lt;enumeration value="Data"/>
 *     &lt;enumeration value="HtmlPart"/>
 *     &lt;enumeration value="Double"/>
 *     &lt;enumeration value="Long"/>
 *     &lt;enumeration value="Xml"/>
 *     &lt;enumeration value="LocalDate"/>
 *     &lt;enumeration value="LocalDateTime"/>
 *     &lt;enumeration value="LocalTime"/>
 *     &lt;enumeration value="DateTime"/>
 *     &lt;enumeration value="ContentId"/>
 *     &lt;enumeration value="GeoPoint"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "dataPropertyType")
@XmlEnum
public enum XmlDataPropertyType {

    @XmlEnumValue("Boolean")
    BOOLEAN("Boolean"),
    @XmlEnumValue("String")
    STRING("String"),
    @XmlEnumValue("Data")
    DATA("Data"),
    @XmlEnumValue("HtmlPart")
    HTML_PART("HtmlPart"),
    @XmlEnumValue("Double")
    DOUBLE("Double"),
    @XmlEnumValue("Long")
    LONG("Long"),
    @XmlEnumValue("Xml")
    XML("Xml"),
    @XmlEnumValue("LocalDate")
    LOCAL_DATE("LocalDate"),
    @XmlEnumValue("LocalDateTime")
    LOCAL_DATE_TIME("LocalDateTime"),
    @XmlEnumValue("LocalTime")
    LOCAL_TIME("LocalTime"),
    @XmlEnumValue("DateTime")
    DATE_TIME("DateTime"),
    @XmlEnumValue("ContentId")
    CONTENT_ID("ContentId"),
    @XmlEnumValue("GeoPoint")
    GEO_POINT("GeoPoint");
    private final String value;

    XmlDataPropertyType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static XmlDataPropertyType fromValue(String v) {
        for (XmlDataPropertyType c: XmlDataPropertyType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}