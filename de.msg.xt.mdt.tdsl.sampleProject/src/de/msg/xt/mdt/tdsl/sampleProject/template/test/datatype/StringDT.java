package de.msg.xt.mdt.tdsl.sampleProject.template.test.datatype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class StringDT {
    @XmlAttribute
    private String value;

    @XmlAttribute
    @XmlJavaTypeAdapter(StringDTEquivalenceClassAdapter.class)
    private StringDTEquivalenceClass equivalenceClass;

    public StringDT() {
    }

    public StringDT(final String value, final StringDTEquivalenceClass equivalenceClass) {
        this();
        this.value = value;
        this.equivalenceClass = equivalenceClass;

    }

    public String getValue() {
        return this.value;
    }

    public StringDTEquivalenceClass getEquivalenceClass() {
        return this.equivalenceClass;
    }
}
