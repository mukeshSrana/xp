//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7-b41 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.12 at 07:12:54 PM CET 
//


package com.enonic.wem.api.xml.model;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

public class XmlRelationshipTypeElem
    extends JAXBElement<XmlRelationshipType>
{

    protected final static QName NAME = new QName("", "relationship-type");

    public XmlRelationshipTypeElem(XmlRelationshipType value) {
        super(NAME, ((Class) XmlRelationshipType.class), null, value);
    }

    public XmlRelationshipTypeElem() {
        super(NAME, ((Class) XmlRelationshipType.class), null, null);
    }

}