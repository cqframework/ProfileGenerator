package org.socraticgrid.quick.fhir.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.AliasRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.CardinalityContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.ComplexTypeRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.ElementRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.EquivalentElementRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.ExtensionContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.HandleElementRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.SimpleTypeRuleContext;
import org.socraticgrid.quick.fhir.profile.FhirTagParser.SkipElementRuleContext;
import org.socraticgrid.uml.OneToOnePropertyMapping;
import org.socraticgrid.uml.TaggedValue;
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlProperty;

public class MappingAnnotationListener extends FhirTagBaseListener {
	
	private final FhirTagParser parser;
	private final OneToOnePropertyMapping mapping;
	private final Map<String, UmlClass> propertySourceMap;
	private String targetResource;

	public MappingAnnotationListener(FhirTagParser parser, UmlProperty sourceProperty) {
		this.parser = parser;
		mapping = new OneToOnePropertyMapping();
		mapping.setSource(sourceProperty);
		propertySourceMap = new HashMap<String, UmlClass>();
	}
	
	public OneToOnePropertyMapping getMapping() {
		return mapping;
	}
	
	public void setDefaultTargetResource(String targetResource) {
		this.targetResource = targetResource;
	}
	
	public void setPropertySource(String key, UmlClass source) {
		propertySourceMap.put(key, source);
	}

	@Override
	public void enterElementRule(ElementRuleContext ctx) {
		mapping.setDestination(mapping.getSource().clone());
		mapping.setSourcePath(ctx.pathExpression(0).getText());
		mapping.setDestinationPath(ctx.pathExpression(1).getText().replaceFirst("DestinationConcept", targetResource));
		mapping.getDestination().setName(getAttributeRelativePath(mapping.getDestinationPath()));
		mapping.getDestination().setSource(getSourceClassForProperty(mapping.getDestinationPath()));
		
	}

	@Override
	public void enterSkipElementRule(SkipElementRuleContext ctx) {
		mapping.setRationale(ctx.getText());
	}

	@Override
	public void enterEquivalentElementRule(EquivalentElementRuleContext ctx) {
		mapping.setDestination(mapping.getSource().clone());
		mapping.getDestination().setSource(getSourceClassForProperty(targetResource));
		mapping.setDestinationPath(targetResource + "." + mapping.getDestination().getName());
		mapping.setSourcePath(mapping.getSource().getSource().getName() + "." + mapping.getSource().getName());
		mapping.setRationale(ctx.getText());
	}

	@Override
	public void enterAliasRule(AliasRuleContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        String newName = null;
        if ( ctx.name()!=null && mapping.getDestination() != null) {
        	newName = tokens.getText(ctx.name());
            mapping.getDestination().setName(newName);
        }
	}

	@Override
	public void enterCardinality(CardinalityContext ctx) {
		TokenStream tokens = parser.getTokenStream();
        String low = null;
        String high = null;
        if ( ctx.INTEGER()!=null && mapping.getDestination() != null) {
        	low = ctx.INTEGER().getText();
        	mapping.getDestination().setLow(Integer.valueOf(low));
        }
        if ( ctx.high()!=null && mapping.getDestination() != null) {
        	high = tokens.getText(ctx.high());
        	high = (high.equals("*"))?"-1":high;
        	mapping.getDestination().setHigh(Integer.valueOf(high));
        }
	}

	@Override
	public void enterExtension(ExtensionContext ctx) {
		TokenStream tokens = parser.getTokenStream();
        Boolean extension = false;
        if ( ctx.BOOLEAN()!=null && mapping.getDestination() != null) {
            extension = Boolean.valueOf(ctx.BOOLEAN().getText());
            mapping.setExtension(extension);
        }
	}

	@Override
	public void enterSimpleTypeRule(SimpleTypeRuleContext ctx) {
		TokenStream tokens = parser.getTokenStream();
        UmlClass type = null;
        if ( ctx.name()!=null && mapping.getDestination() != null) {
        	type = new UmlClass(ctx.name().getText());//TODO do a model lookup rather than create a new class
            mapping.getDestination().addType(type);
        }
	}
	
	@Override
	public void enterComplexTypeRule(ComplexTypeRuleContext ctx) {
		mapping.getDestination().getTypes().clear(); //NOTE: If a type is specified in the tag, original types will be cleared.
	}

	protected UmlClass getSourceClassForProperty(String path) {//TODO Fix so that class is shared across all properties created thus
		String source = getContainingResource(path);
		UmlClass sourceClass = propertySourceMap.get(source);
		if(sourceClass == null) {
			sourceClass = new UmlClass(source);
			propertySourceMap.put(source, sourceClass);
		}
		return sourceClass;
	}
	
	public static String getAttributeRelativePath(String canonicalPath) {
		int index = canonicalPath.indexOf('.');
		String relativePath = canonicalPath;
		if(index > 0) {
			relativePath = canonicalPath.substring(index + 1);
		}
		return relativePath;
	}
	
	public static String getContainingResource(String canonicalPath) {
		int index = canonicalPath.indexOf('.');
		String resourceName = canonicalPath;
		if(index > 0) {
			resourceName = canonicalPath.substring(0,index);
		}
		return resourceName;
	}
	
	/**
	 * Helper method to set up parser and error listener.
	 * 
	 * @param input
	 * @return
	 */
	public static FhirTagParser setUpParser(String input, FhirTagParseErrorListener errorListener) {
		FhirTagLexer lexer = new FhirTagLexer(new ANTLRInputStream(input));
		FhirTagParser parser = new FhirTagParser(new CommonTokenStream(lexer));
		parser.removeErrorListeners();
		if(errorListener != null) {
			parser.addErrorListener(errorListener);
		}
		return parser;
	}
	
	/**
	 * Helper method to set up listener
	 * 
	 * @param parser
	 * @param sourceProperty
	 * @return
	 */
	public static OneToOnePropertyMapping retrieveMapping(FhirTagParser parser, UmlProperty sourceProperty, UmlClass targetResource) {
		HandleElementRuleContext ctx = parser.handleElementRule();
		return retrieveMapping(parser, ctx, sourceProperty, targetResource);
	}
	
	/**
	 * Helper method to set up listener
	 * 
	 * @param parser
	 * @param context
	 * @param sourceProperty
	 * @return
	 */
	public static OneToOnePropertyMapping retrieveMapping(FhirTagParser parser, ParserRuleContext context, UmlProperty sourceProperty, UmlClass targetResource) {
		ParseTreeWalker walker = new ParseTreeWalker();
	    MappingAnnotationListener annotationListener = new MappingAnnotationListener(parser, sourceProperty);
	    annotationListener.setDefaultTargetResource(targetResource.getName());
	    annotationListener.setPropertySource(targetResource.getName(), targetResource);
	    walker.walk(annotationListener, context);
	    return annotationListener.getMapping();
	}
	
	/**
	 * Generates and collects the FHIR mappings for the UML property argument based on its 
	 * associated annotation.
	 * 
	 * @param property
	 * @return
	 */
	public static List<OneToOnePropertyMapping> getPropertyMappings(UmlProperty property, UmlClass targetResource, FhirTagParseErrorListener errorListener) {
		List<OneToOnePropertyMapping> mappings = new ArrayList<OneToOnePropertyMapping>();
		List<TaggedValue> tags = property.getTagsWithPrefix("profile.fhir.element");
		for(TaggedValue tag : tags) {
			String mappingString = tag.toString();
			FhirTagParser parser = MappingAnnotationListener.setUpParser(mappingString, errorListener);
			mappings.add(MappingAnnotationListener.retrieveMapping(parser, property, targetResource));
		}
		return mappings;
	}
}
