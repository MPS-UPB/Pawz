import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import com.sun.xml.xsom.*;
import com.sun.xml.xsom.parser.XSOMParser;

public class XSDParser {

	class SimpleTypeRestriction {
		public String[] enumeration = null;
		public String maxValue = null;
	    public String minValue = null;
	    public String length = null;
	    public String maxLength = null;
	    public String minLength = null;
	    public String pattern = null;
	    public String totalDigits = null;
	}
		
	XSSchemaSet schemaSet;
	XSSchema xsSchema;
	SimpleTypeRestriction execType;
	SimpleTypeRestriction execName;
	
	private void initRestrictions (XSSimpleType xsSimpleType, SimpleTypeRestriction t) {
		XSRestrictionSimpleType restriction = xsSimpleType.asRestriction();
		if (restriction != null) {
			Vector<String> enumeration = new Vector<String>();
	        Iterator<? extends XSFacet> i = restriction.getDeclaredFacets().iterator();
	        while(i.hasNext()){
	            XSFacet facet = i.next();
	            if(facet.getName().equals(XSFacet.FACET_ENUMERATION)){
	                enumeration.add(facet.getValue().value);
	            }
	            if(facet.getName().equals(XSFacet.FACET_MAXINCLUSIVE)){
	                t.maxValue = facet.getValue().value;
	            }
	            if(facet.getName().equals(XSFacet.FACET_MININCLUSIVE)){
	                t.minValue = facet.getValue().value;
	            }
	            if(facet.getName().equals(XSFacet.FACET_MAXEXCLUSIVE)){
	                t.maxValue = String.valueOf(Integer.parseInt(facet.getValue().value) - 1);
	            }
	            if(facet.getName().equals(XSFacet.FACET_MINEXCLUSIVE)){
	                t.minValue = String.valueOf(Integer.parseInt(facet.getValue().value) + 1);
	            }
	            if(facet.getName().equals(XSFacet.FACET_LENGTH)){
	                t.length = facet.getValue().value;
	            }
	            if(facet.getName().equals(XSFacet.FACET_MAXLENGTH)){
	                t.maxLength = facet.getValue().value;
	            }
	            if(facet.getName().equals(XSFacet.FACET_MINLENGTH)){
	                t.minLength = facet.getValue().value;
	            }
	            if(facet.getName().equals(XSFacet.FACET_PATTERN)){
	                t.pattern = facet.getValue().value;
	            }
	            if(facet.getName().equals(XSFacet.FACET_TOTALDIGITS)){
	                t.totalDigits = facet.getValue().value;
	            }
	        }
	        if(enumeration.size() > 0){
	            t.enumeration = enumeration.toArray(new String[]{});
	        }
		}
	}
	
	private void setExecType (SimpleTypeRestriction _simpleType) {
		this.execType = _simpleType;
	}
	
	public void setExecName (SimpleTypeRestriction _simpleType) {
		this.execName = _simpleType;
	}
	
	public String getExecType () {
		return execType.pattern;
	}
	
	public String getExecName () {
		return execName.pattern;
	}
	
	public XSParticle[] parseXSD (File file) {
		XSOMParser parser = new XSOMParser();
		
		try {
	        parser.parse(file);
	        this.schemaSet = parser.getResult();
	        this.xsSchema = this.schemaSet.getSchema(1);

			XSSimpleType execNameST = this.xsSchema.getSimpleType("execName");
			XSSimpleType execTypeST = this.xsSchema.getSimpleType("execType");
			XSSimpleType execDescST = this.xsSchema.getSimpleType("execDescription");
			
			SimpleTypeRestriction execNameRestr = new SimpleTypeRestriction();
			SimpleTypeRestriction execTypeRestr = new SimpleTypeRestriction();
			SimpleTypeRestriction execDescRestr = new SimpleTypeRestriction();
			
			initRestrictions(execNameST, execNameRestr);
			this.setExecName(execNameRestr);
			initRestrictions(execTypeST, execTypeRestr);
			this.setExecType(execTypeRestr);
			initRestrictions(execDescST, execDescRestr);
			
		    if (this.xsSchema.getElementDecls().size() != 1)
		    {
		      throw new Exception("Should be only elment type per file.");
		    }
			
		    XSElementDecl ed = this.xsSchema.getElementDecls().values()
		            .toArray(new XSElementDecl[0])[0];
		    XSContentType xsContentType = ed.getType().asComplexType().getContentType();
		    XSParticle particle = xsContentType.asParticle();
		    
		    if (particle != null) {
		    	XSTerm term = particle.getTerm();
		    	if (term.isModelGroup()) {
		    		XSModelGroup xsModelGroup = term.asModelGroup();
		    		term.asElementDecl();
		    		XSParticle[] particles = xsModelGroup.getChildren();

		    		return particles;
		    	}
		    }
	    }
	    catch (Exception exp) {
	        exp.printStackTrace(System.out);
	    }
		
		return null;
	}
}
